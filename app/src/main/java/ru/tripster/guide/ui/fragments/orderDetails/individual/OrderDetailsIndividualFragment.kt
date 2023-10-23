package ru.tripster.guide.ui.fragments.orderDetails.individual


import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Patterns
import android.view.*
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.events.OrderDetailsForChat
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.domain.model.events.Status
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.domain.model.order.PerTicketOrderDetail
import ru.tripster.guide.Constants
import ru.tripster.guide.R
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_BACK_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_CALL_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_CHAT_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_CHAT_PREVIEW
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_COMMUNICATE_TIPS
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_CONFIRM_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_CONTACT_SUPPORT
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_GET_CONTACT_TIP
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_MENU_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_ORDER_CANCEL
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_ORDER_CHANGE_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.PRIVATE_EXP_SHOW_TRAVELLER_CONTACT
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentOrderDetailsIndividualBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment.Companion.AMPLITUDE_BUNDLE
import ru.tripster.guide.ui.MainFragment.Companion.AMPLITUDE_KEY
import ru.tripster.guide.ui.fragments.chat.RulesOfViewContactBottomSheet
import ru.tripster.guide.ui.fragments.confirmOrder.ConfirmOrEditOrderFragment
import java.util.*


class OrderDetailsIndividualFragment :
    FragmentBaseNCMVVM<OrderDetailsIndividualViewModel, FragmentOrderDetailsIndividualBinding>() {
    override val viewModel: OrderDetailsIndividualViewModel by viewModel()
    override val binding: FragmentOrderDetailsIndividualBinding by viewBinding()
    private val args: OrderDetailsIndividualFragmentArgs by navArgs()
    private var statisticsData = StatisticsData.empty()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).transparentStatusBar()

        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (args.isNavigatedDateOrder) {
                navigateFragment(OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToOrderFragment())
            } else popBackStack()
        }
    }

    override fun onView() {

        activity?.setStatusBarWhiteTextFullScreen()
        activity?.bottomNavBarVisibility(true)

        if (viewModel.currentProgress == 1.0f) {
            binding.toolbar.alpha = 1.0f
            activity?.setFullScreenAndLightStatusBar()
        }

        setMarginToMotionLayoutItem(R.id.start)
        setMarginToMotionLayoutItem(R.id.end)

        viewModel.setOrderCurrentEvent(args.eventId, getCurrentDateAndTime())
        setProgress()

        activity?.supportFragmentManager?.setFragmentResultListener(
            AMPLITUDE_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getInt(AMPLITUDE_BUNDLE)
            context?.let {
                viewModel.experienceClickedIndividual(
                    it,
                    PRIVATE_EXP_MENU_BUTTON,
                    result.menuItem()
                )
            }
        }

        setFragmentResultListener(
            ConfirmOrEditOrderFragment.CONFIRM_ORDER_REQUEST_KEY
        )
        { _, bundle ->
            val result =
                bundle.getParcelable<OrderDetails>(ConfirmOrEditOrderFragment.CONFIRM_ORDER_BUNDLE)
            viewModel.showConfirmButton = false
            if (result?.isConfirm == true) {
                context?.getString(R.string.order_confirmed)
                    ?.let { Toast(context).showCustomToast(it, this, true) }
            } else {
                context?.getString(R.string.order_edited)
                    ?.let { Toast(context).showCustomToast(it, this, true) }
            }
            viewModel.getOrderDetails(args.orderId)
        }

        setFragmentResultListener(RulesOfViewContactBottomSheet.PRESS_SHOW_CONTACTS_KEY) { _, bundle ->
            context?.let {
                viewModel.experienceClickedIndividual(it, PRIVATE_EXP_SHOW_TRAVELLER_CONTACT)
            }

            viewModel.resultOfBottomSheet =
                bundle.getBoolean(RulesOfViewContactBottomSheet.PRESS_SHOW_CONTACTS_BUNDLE)
            if (viewModel.resultOfBottomSheet) {
                viewModel.getOrderDetails(args.orderId)
            }
        }

        with(binding) {
//            nestedScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
//                val bottomY = confirmOrder.getLocationOnScreen().y - confirmOrder.measuredHeight
//                val stickyButtonY =
//                    btnConfirmOrderSticky.getLocationOnScreen().y - btnConfirmOrderSticky.measuredHeight
//
//                if (viewModel.showConfirmButton) {
//                    if (bottomY < stickyButtonY) {
//                        btnConfirmOrderSticky.visibility = View.GONE
//                        confirmOrder.visibility = View.VISIBLE
//
//                    } else {
//                        btnConfirmOrderSticky.visibility = View.VISIBLE
//                        confirmOrder.visibility = View.GONE
//                    }
//
//                }
//
//            }

            backButton.setOnClickListener {
                context?.let {
                    viewModel.experienceClickedIndividual(it, PRIVATE_EXP_BACK_ICON)
                }

                if (args.isNavigatedDateOrder) {
                    navigateFragment(OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToOrderFragment())
                } else popBackStack()
            }
        }

        if (context?.isOnline() == true) {
            viewModel.getOrderDetails(args.orderId)
        } else {
            errorMessage(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        setFragmentResult(IS_MODIFIED_KEY, bundleOf(IS_MODIFIED_BUNDLE to viewModel.isModified))
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.getOrderDetails(args.orderId)
    }

    override fun onViewClick() {
        with(binding) {
            motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionStarted(motionLayout: MotionLayout, i: Int, i1: Int) {
                    viewModel.currentProgress =
                        if (motionLayout.progress in 0.75f..1.0f) 1.0f else 0.0f
                }

                @Suppress("DEPRECATION")
                override fun onTransitionChange(
                    motionLayout: MotionLayout,
                    i: Int,
                    i1: Int,
                    v: Float
                ) {
//                    confirmButtonVisible()
                    when (motionLayout.progress) {
                        in 0.0f..0.75f -> {
                            setMarginToMotionLayoutItem(R.id.start)
                            statusTv.makeVisible()
                            numberTv.makeVisible()
                            titleTv.makeVisible()
                            toolbar.alpha = 0f
                            activity?.setStatusBarWhiteTextFullScreen()
                            viewModel.currentProgress = 0.0f
                        }

                        in 0.75f..1.0f -> {
                            statusTv.makeVisibleGone()
                            numberTv.makeVisibleGone()
                            titleTv.makeVisibleGone()
                            activity?.setFullScreenAndLightStatusBar()
                            toolbar.alpha = motionLayout.progress
                            setMarginToMotionLayoutItem(R.id.end)
                            viewModel.currentProgress = 1.0f
                        }
                    }

                    divider.alpha = 0f
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                    setProgress()
//                    confirmButtonVisible()
                    divider.alpha = if (motionLayout.progress == 1f) 1f else 0f
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout,
                    i: Int,
                    b: Boolean,
                    v: Float
                ) {
//                    confirmButtonVisible()
                }
            })

            phoneNumber.setOnClickListener {
                context?.let { context ->
                    viewModel.orderDetailsValue?.traveler?.phone?.phoneCall(
                        context
                    )
                }
            }

            emailAddress.setOnClickListener {
                startActivity(
                    viewModel.orderDetailsValue?.traveler?.email?.openMail()
                )
            }

            contactSupport.setOnClickListener {
                context?.let {
                    viewModel.experienceClickedIndividual(it, PRIVATE_EXP_CONTACT_SUPPORT)
                }

                if (!viewModel.isNavigated) {
                    context?.let { context ->
                        this@OrderDetailsIndividualFragment.contactSupportBottomSheet(
                            context, args.orderId, false, viewModel.currentProgress, {
                                lifecycleScope.launch {
                                    delay(1500)
                                    it()
                                }
                            }
                        ) {
                            viewModel.isNavigated = false
                        }
                    }

                    viewModel.isNavigated = true
                }
            }

            confirmOrder.setOnClickListener {
                context?.let {
                    viewModel.experienceClickedIndividual(it, PRIVATE_EXP_CONFIRM_BUTTON)
                }

                viewModel.orderDetailsValue?.let { navigateToConfirmOrEdit(true) }
            }

//            btnConfirmOrderSticky.setOnClickListener {
//                viewModel.orderDetailsValue?.let {
//                    navigateToConfirmOrEdit(
//                        true
//                    )
//                }
//            }
            editOrder.setOnClickListener {
                context?.let {
                    viewModel.experienceClickedIndividual(it, PRIVATE_EXP_ORDER_CHANGE_BUTTON)
                }

                viewModel.orderDetailsValue?.let { navigateToConfirmOrEdit(false) }
            }

            cancelOrder.setOnClickListener {

                if (!viewModel.isNavigated) {
                    context?.let {
                        viewModel.experienceClickedIndividual(it, PRIVATE_EXP_ORDER_CANCEL)
                    }
                    viewModel.orderDetailsValue?.experience?.title?.let { navigateToCancel(it) }
                    viewModel.isNavigated = true
                }
            }

            userDetails.phone.setOnClickListener {

                context?.let {
                    viewModel.experienceClickedIndividual(it, PRIVATE_EXP_CALL_ICON)
                }

                if (viewModel.orderDetailsValue?.status != Status.PAID.value) {
                    if (statisticsData.ordersRateValue >= 60) {
                        navigateFragment(
                            OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToRulesOfViewContactBottomSheet(
                                args.orderId,
                                context?.getString(R.string.private_phone_type) ?: "",
                                viewModel.orderDetailsValue?.traveler?.phone.toString()
                            )
                        )
                    } else {
                        navigateFragment(
                            OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToSeeContactWarningBottomSheet(
                                statisticsData,
                                context?.getString(R.string.private_phone_type) ?: ""
                            )
                        )
                    }
                } else context?.let { it1 ->
                    viewModel.orderDetailsValue?.traveler?.phone?.phoneCall(
                        it1
                    )
                }
            }

            userDetails.message.setOnClickListener {
                context?.let {
                    viewModel.experienceClickedIndividual(it, PRIVATE_EXP_CHAT_ICON)
                }
                navigateToChat(peopleText.text.toString(), paymentText.text.toString())
            }

            userDetails.messageContent.setOnClickListener {
                context?.let {
                    viewModel.experienceClickedIndividual(it, PRIVATE_EXP_CHAT_PREVIEW)
                }

                navigateToChat(peopleText.text.toString(), paymentText.text.toString())
            }

            error.update.setOnClickListener {
                isOnlineAgain()
            }

        }
    }

    override fun onEach() {
        onEach(viewModel.orderDetails) { orderDetails ->
            viewModel.orderDetailsValue = orderDetails

            with(binding) {
                toolbar.setTitle(orderDetails?.experience?.title ?: "")
                toolbar.setNumber(orderDetails?.id ?: 0)
                toolbar.setStatus(
                    orderDetails?.status ?: "",
                    orderDetails?.event?.awareStartDt,
                    binding.checkIcon,
                    orderDetails?.reject?.initiator ?: ""
                )

                if (viewModel.lastModifiedDate == "") {
                    viewModel.lastModifiedDate =
                        orderDetails?.lastModifiedDate.toString()
                } else {
                    viewModel.isModified =
                        orderDetails?.lastModifiedDate != viewModel.lastModifiedDate
                    viewModel.lastModifiedDate = orderDetails?.lastModifiedDate.toString()
                }
                titleTv.text = orderDetails?.experience?.title
                context?.let { context ->
                    Glide.with(context).load(orderDetails?.experience?.coverImage)
                        .centerCrop()
                        .into(ivToolbarImage)
                }
                numberTv.text = context?.resources?.getString(R.string.number, orderDetails?.id)

                with(userDetails) {
                    if (orderDetails?.traveler?.avatar150Url?.isEmpty() == false) {
                        avatar.setStrokeColorResource(R.color.ten_percent_of_black)
                        avatar.strokeWidth = 2f
                    }

                    context?.let { context ->
                        Glide.with(context).load(orderDetails?.traveler?.avatar150Url)
                            .placeholder(R.drawable.ic_avatar_icon_placeholder)
                            .centerCrop()
                            .into(avatar)
                    }

                    name.text = orderDetails?.traveler?.name?.shortedSurname()
                    details.text = context?.getStringRes(R.string.traveler)
                    messageLogic(orderDetails?.lastMessage?.comment)
                }
            }


            orderDetails?.status?.statusType(
                binding.statusTv,
                context,
                orderDetails.event.awareStartDt,
                orderDetails.reject.initiator,
                true,
                context?.getString(R.string.private_type) ?: "",
                viewModel.currentProgress
            ) {
                if (it) binding.checkIcon.visibility = View.VISIBLE
            }
            dateAndHourLogic(
                orderDetails?.event?.date,
                orderDetails?.event?.time,
                orderDetails?.experience?.duration
            )
            peopleCountLogic(orderDetails?.personsCount, orderDetails?.price?.perPicket)
            priceLogic(
                orderDetails?.price?.value?.toInt(),
                orderDetails?.price?.prePay?.toInt(),
                orderDetails?.price?.paymentToGuide?.toInt(),
                orderDetails?.price?.currency?.currency()
            )
            meetingPlace(orderDetails?.event?.meetingPoint?.description)
            messageCountLogic(orderDetails?.newMessagesCount)

            if (orderDetails?.event?.awareStartDt?.check24Hours() == true && orderDetails.status == Status.PAID.value) {
                with(binding) {
                    userContactsInfo.isVisible = true
                    userContactsInfo.text =
                        context?.getString(R.string.info_details_cancelled_ended)
                    phoneNumber.isVisible = false
                    emailAddress.isVisible = false
                    userDetails.phone.isVisible = false
                }
            } else {
                with(binding) {
                    userContactsInfo.isVisible = false
                    userContactsInfo.text =
                        context?.getString(R.string.info_details_cancelled_ended)
                    phoneNumber.isVisible = true
                    emailAddress.isVisible = true
                    userDetails.phone.isVisible = true
                }
                contactLogic(orderDetails?.traveler?.email, orderDetails?.traveler?.phone)
            }
            rejectLogic(
                orderDetails?.newMessagesCount ?: 0,
                orderDetails?.reject?.initiator.toString(),
                orderDetails?.reject?.reason_text,
                orderDetails?.reject?.message,
                orderDetails?.reject?.reason
            )
            buttonLogic(orderDetails?.status)
            discountLogic(
                orderDetails?.discountRate,
                orderDetails?.price?.value,
                orderDetails?.price?.currency?.currency()
            )
            shimmerVisibility()

        }

        onEach(viewModel.orderDetailsError) {
            errorMessage(true)
        }
    }

    private fun shimmerVisibility() {
        with(binding) {
            shimmer.layoutShimmer.makeVisibleGone()
            containerShimmer.isClickable = false
            containerShimmer.isFocusable = false

            val vto: ViewTreeObserver = containerDetails.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (isAdded) {
                        containerDetails.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val height: Int = containerDetails.measuredHeight

                        context?.let {
                            nestedScrollView.isNestedScrollingEnabled =
                                height + ivToolbarImage.measuredHeight - it.dpToPx(R.dimen.space_16) >= containerShimmer.measuredHeight
                        }
                    }
                }
            })

            containerShimmer.makeVisibleGone()
        }
    }


    private fun messageLogic(message: String?) {
        with(binding.userDetails) {
            if (!message.isNullOrEmpty()) {
                val listOfWord = message.split(" ").toMutableList()
                listOfWord.forEachIndexed { index, word ->
                    word.replace("-", "")
                    if (checkPhone(word)) {
                        viewModel.booleanOfContacts = true
                        hideContacts(listOfWord, word, index, viewModel.booleanOfContacts).let {

                            if (viewModel.messageContentWidth == 0)
                                viewModel.messageContentWidth = messageContent.setResizableText(
                                    it,
                                    3,
                                    true,
                                    tvWidth = viewModel.messageContentWidth
                                ) else messageContent.setResizableText(
                                it,
                                3,
                                true,
                                tvWidth = viewModel.messageContentWidth
                            )
                        }

                        messageContent.makeVisible()
                        exclude.makeVisible()
                    } else {
                        viewModel.booleanOfContacts = false
                        hideContacts(listOfWord, word, index, viewModel.booleanOfContacts).let {

                            if (viewModel.messageContentWidth < messageContent.setResizableText(
                                    it,
                                    3,
                                    true,
                                    tvWidth = viewModel.messageContentWidth
                                )
                            ) {
                                viewModel.messageContentWidth = messageContent.setResizableText(
                                    it,
                                    3,
                                    true,
                                    tvWidth = viewModel.messageContentWidth
                                )
                            } else messageContent.setResizableText(
                                it,
                                3,
                                true,
                                tvWidth = viewModel.messageContentWidth
                            )
                        }

                        messageContent.makeVisible()
                        exclude.makeVisible()
                    }
                }

            } else {
                messageContent.makeVisibleGone()
                exclude.makeVisibleGone()
            }
        }
    }

    private fun navigateToChat(peopleText: String, paymentText: String) {
        val memberCount = if (viewModel.orderDetailsValue?.personsCount != 0)
            context?.getString(
                R.string.user_details_chat,
                peopleText,
                paymentText
            ) else context?.getString(R.string.no_people_count_tour)

        navigateFragment(
            OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToChatFragment(
                Gson().toJson(
                    OrderDetailsForChat(
                        viewModel.orderDetailsValue?.id ?: 0,
                        viewModel.orderDetailsValue?.status ?: "",
                        viewModel.orderDetailsValue?.experience?.title ?: "",
                        context?.getString(R.string.private_type) ?: "",
                        viewModel.orderDetailsValue?.event?.date?.dateFormatting() + " " + viewModel.orderDetailsValue?.event?.time?.hourAndDuration(
                            viewModel.orderDetailsValue?.experience?.duration
                        ),
                        viewModel.orderDetailsValue?.event?.awareStartDt?.check24Hours()
                            ?: false,
                        viewModel.orderDetailsValue?.traveler?.phone ?: "",
                        viewModel.orderDetailsValue?.traveler?.name ?: "",
                        viewModel.orderDetailsValue?.traveler?.avatar150Url ?: "",
                        memberCount ?: "",
                        viewModel.orderDetailsValue?.price?.currency ?: "",
                        viewModel.orderDetailsValue?.newMessagesCount != 0
                    )
                ), context?.getString(R.string.private_type) ?: "",
                Gson().toJson(
                    StatisticsData(
                        statisticsData.ordersRateValue,
                        statisticsData.bookingRateValue,
                        statisticsData.confirmRateValue,
                        statisticsData.canOpenContacts
                    )
                )
            )
        )
    }

    private fun hideContacts(
        listOfWord: MutableList<String>,
        word: String,
        index: Int,
        booleanOfContacts: Boolean
    ): String {
        var closedWord = ""
        var string = ""
        var lengthOfWord = word.length
        if (booleanOfContacts) {
            while (lengthOfWord != 0) {

                closedWord = "$closedWord*"
                lengthOfWord--
            }
            listOfWord[index] = closedWord
        }

        listOfWord.forEach {
            string += "$it "
        }
        return string
    }

    private fun messageCountLogic(newMessageCount: Int?) {
        with(binding.userDetails) {
            if (newMessageCount != 0) {
                messageCount.text = newMessageCount.toString()
                messageCount.visibility = View.VISIBLE
            } else {
                messageCount.visibility = View.GONE
            }
        }
    }

    private fun checkPhone(phoneNumber: String): Boolean =
        Patterns.PHONE.matcher(phoneNumber)
            .matches() && phoneNumber.length >= 10

    private fun dateAndHourLogic(date: String?, hour: String?, duration: Double?) {
        with(binding) {
            if (!date.isNullOrEmpty()) {
                tourDate.text = date.dateFormatting()
                tourHour.makeVisible()
                noDateInfo.makeVisibleGone()
                hourLogic(hour, duration, date)
            } else {
                tourDate.text = context?.getStringRes(R.string.no_date_and_time)
                noDateInfo.apply {
                    makeVisible()
                    text = context?.getStringRes(R.string.no_date_and_time_info)
                    setMargins(null, R.dimen.space_4, null, null)
                    initTextLinks(
                        noDateInfo.text.toString(),
                        context,
                        context.getString(R.string.no_date_and_time_info_colored)
                    ) {
                        context?.let {
                            viewModel.experienceClickedIndividual(it, PRIVATE_EXP_COMMUNICATE_TIPS)
                        }

                        startActivity((context.getString(R.string.BASE_URL) + Constants.ADVICE_TO_TOURIST_LINK).openLinkInBrowser())
                    }
                }
                tourHour.makeVisibleGone()
            }
        }
    }

    private fun hourLogic(hour: String?, duration: Double?, date: String?) {
        with(binding.tourHour) {
            text = if (!hour.isNullOrEmpty()) {
                setTextColor(context.getColor(R.color.gray_20))
                hour.hourAndDuration(date, duration)
            } else {
                setTextColor(context.getColor(R.color.gray))
                context?.getStringRes(R.string.hour_is_missing)
            }
        }
    }

    private fun peopleCountLogic(count: Int?, perTicket: List<PerTicketOrderDetail>?) {
        with(binding) {
            val set = ConstraintSet()
            val paramsContainerPeople =
                containerPeople.layoutParams as ConstraintLayout.LayoutParams

            if (count != null && count != 0) {
                containerPeopleInfo.makeVisible()
                paramsContainerPeople.width = 0
                set.clone(containerDetails)

                containerPeople.gravity = Gravity.CENTER or Gravity.START
                containerPeople.layoutParams = paramsContainerPeople

                set.connect(
                    containerPeople.id, ConstraintSet.END,
                    guideline.id, ConstraintSet.START
                )

                peopleText.text = resources.getQuantityString(
                    R.plurals.people_count_tour, count, count
                )

                if (!perTicket.isNullOrEmpty()) {
                    ticket.makeVisibleGone()
                    containerPeopleInfo.removeAllViews()
                    perTicket.forEach {
                        val type = context?.let { context -> AppCompatTextView(context) }
                        type?.setTextAppearance(R.style.Text_17_400)
                        type?.text = context?.getString(R.string.ticket_type, it.count, it.title)
                        containerPeopleInfo.addView(type)
                    }
                }
            } else {
                containerPeopleInfo.makeInVisible()
                paramsContainerPeople.width = LinearLayout.LayoutParams.WRAP_CONTENT

                containerPeople.gravity = Gravity.CENTER
                containerPeople.layoutParams = paramsContainerPeople

                set.clear(
                    containerPeople.id, ConstraintSet.END
                )

                ticket.makeInVisible()
                peopleText.text = context?.getStringRes(R.string.no_people_count_tour)
            }

            set.applyTo(containerDetails)
        }
    }

    private fun priceLogic(value: Int?, prePay: Int?, guide: Int?, currency: String?) {
        with(binding) {
            if (value != null) {
                paymentText.text =
                    context?.getString(R.string.price_value)
                        ?.setParticipantAndCurrency(null, value, currency)

                priceTripster.apply {
                    text = context?.getString(R.string.price_on_tripster)
                        ?.setTripsterCurrency(prePay, currency?.currency())
                }
                priceToGuide.text =
                    context?.getString(R.string.price_to_guide)
                        ?.setTripsterCurrency(guide, currency?.currency())
                containerPayment.visibility = View.VISIBLE
                priceTripster.visibility = View.VISIBLE
                priceToGuide.visibility = View.VISIBLE
            } else {
                containerPayment.visibility = View.GONE
                priceTripster.visibility = View.GONE
                priceToGuide.visibility = View.GONE
            }
        }
    }

    private fun meetingPlace(place: String?) {
        with(binding) {
            if (!place.isNullOrEmpty()) {
                place.let {

                    if (viewModel.meetingPointWidth < meetingPlaceInfo.setResizableText(
                            it,
                            7,
                            viewMore = viewModel.meetingPointViewMoreClicked,
                            isViewMoreClickable = true,
                            tvWidth = viewModel.meetingPointWidth
                        )
                    ) {
                        viewModel.meetingPointWidth =
                            meetingPlaceInfo.setResizableText(
                                it,
                                7,
                                viewMore = viewModel.meetingPointViewMoreClicked,
                                isViewMoreClickable = true,
                                tvWidth = viewModel.meetingPointWidth
                            )
                    } else meetingPlaceInfo.setResizableText(
                        it,
                        7,
                        viewMore = viewModel.meetingPointViewMoreClicked,
                        isViewMoreClickable = true,
                        tvWidth = viewModel.meetingPointWidth
                    )
                }
                place.let {
                    meetingPlaceInfo.setResizableText(
                        it,
                        7,
                        viewModel.meetingPointViewMoreClicked
                    ) {
                        viewModel.meetingPointViewMoreClicked = false
//                        confirmButtonVisible()
                    }
                }

                meetingPlace.visibility = View.VISIBLE
                meetingPlaceInfo.visibility = View.VISIBLE
            } else {
                meetingPlace.visibility = View.GONE
                meetingPlaceInfo.visibility = View.GONE
            }

        }
    }

    private fun contactLogic(email: String?, phone: String?) {
        with(binding) {
            if (!email.isNullOrEmpty() && !phone.isNullOrEmpty()) {
                when (viewModel.orderDetailsValue?.status) {
                    Status.PAID.value -> {
                        phoneNumber.text = phone
                        emailAddress.text = email
                        phoneNumber.makeVisible()
                        emailAddress.makeVisible()
                        userContactsInfo.makeVisibleGone()
                    }
                    else -> {
                        if (statisticsData.ordersRateValue >= 60) {
                            if (viewModel.orderDetailsValue?.travelerContactsOpen == false) {
                                userDetails.phone.makeVisible()
                                phoneNumber.makeVisibleGone()
                                emailAddress.makeVisibleGone()
                                userContactsInfo.makeVisible()
                                context?.let {
                                    userContactsInfo.initTextLinks(
                                        resources.getString(R.string.contact_available_after_payment),
                                        it, resources.getString(R.string.show_contacts)
                                    ) {
                                        navigateFragment(
                                            OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToRulesOfViewContactBottomSheet(
                                                args.orderId,
                                                context?.getString(R.string.private_type) ?: "",
                                                ""
                                            )
                                        )
                                    }
                                }
                            } else {
                                phoneNumber.text = phone
                                emailAddress.text = email
                                phoneNumber.makeVisible()
                                emailAddress.makeVisible()
                                userContactsInfo.makeVisibleGone()
                            }

                        } else {
                            phoneNumber.makeVisibleGone()
                            emailAddress.makeVisibleGone()
                            userContactsInfo.makeVisible()
                            context?.let {
                                userContactsInfo.initTextLinks(
                                    resources.getString(R.string.user_contact_info),
                                    it, resources.getString(R.string.user_contact_info_colored)
                                ) {
                                    viewModel.experienceClickedIndividual(
                                        it,
                                        PRIVATE_EXP_GET_CONTACT_TIP
                                    )
                                    navigateFragment(
                                        OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToSeeContactWarningBottomSheet(
                                            statisticsData,
                                            context?.getString(R.string.private_type) ?: ""
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                when {
                    viewModel.orderDetailsValue?.status == Status.CANCELLED.value -> {
                        phoneNumber.makeVisibleGone()
                        emailAddress.makeVisibleGone()
                        userContactsInfo.makeVisible()
                        userDetails.phone.makeVisibleGone()
                        userContactsInfo.text =
                            context?.getString(R.string.info_details_cancelled_ended)
                    }
                    statisticsData.ordersRateValue < 60 -> {
                        phoneNumber.makeVisibleGone()
                        emailAddress.makeVisibleGone()
                        userContactsInfo.makeVisible()
                        context?.let {
                            userContactsInfo.initTextLinks(
                                resources.getString(R.string.user_contact_info),
                                it, resources.getString(R.string.user_contact_info_colored)
                            ) {
                                navigateFragment(
                                    OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToSeeContactWarningBottomSheet(
                                        statisticsData,
                                        context?.getString(R.string.private_type) ?: ""
                                    )
                                )
                            }
                        }
                    }
                    else -> {
                        userDetails.phone.makeVisible()
                        userContactsInfo.makeVisible()
                        phoneNumber.makeVisibleGone()
                        emailAddress.makeVisibleGone()
                        userContactsInfo.text = context?.getStringRes(R.string.user_contact_info)
                        context?.let {
                            userContactsInfo.initTextLinks(
                                resources.getString(R.string.contact_available_after_payment),
                                it, resources.getString(R.string.show_contacts)
                            ) {
                                navigateFragment(
                                    OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToRulesOfViewContactBottomSheet(
                                        args.orderId,
                                        context?.getString(R.string.private_type) ?: "",
                                        ""
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun rejectLogic(
        newMessageCount: Int,
        initiator: String,
        reason: String?,
        message: String?,
        reasonType: String?
    ) {
        with(binding) {
            when {
                reason.isNullOrEmpty() && message.isNullOrEmpty() -> {
                    containerCancel.visibility =
                        View.GONE
                    containerUserDetails.setMargins(null, R.dimen.space_0, null, null)
                }
                !reason.isNullOrEmpty() && !message.isNullOrEmpty() -> {
                    containerCancel.visibility = View.VISIBLE
                    containerUserDetails.setMargins(null, R.dimen.space_24, null, null)
                    cancelReasonTitle.visibility = View.VISIBLE
                    cancelReasonTitle.text =
                        if (reasonType == context?.getStringRes(R.string.guide_duplicated_order)) context?.getString(
                            R.string.guide_duplicated_order_txt
                        ) else reason
                    if (initiator != context?.getString(R.string.traveler_type)) {
                        cancelReason.visibility = View.VISIBLE
                        cancelReason.text = message
                    } else if (newMessageCount != 0) {
                        userDetails.messageContent.makeVisible()
                        userDetails.messageContent.text = message
                        userDetails.exclude.makeVisible()
                    }
                }

                !reason.isNullOrEmpty() && message.isNullOrEmpty() -> {
                    containerCancel.visibility = View.VISIBLE
                    cancelReasonTitle.visibility = View.VISIBLE
                    containerUserDetails.setMargins(null, R.dimen.space_24, null, null)
                    cancelReason.visibility = View.GONE
                    cancelReasonTitle.text =
                        if (reasonType == context?.getStringRes(R.string.guide_duplicated_order)) context?.getString(
                            R.string.guide_duplicated_order_txt
                        ) else reason
                }

                reason.isNullOrEmpty() && !message.isNullOrEmpty() -> {
                    containerCancel.visibility = View.VISIBLE
                    cancelReasonTitle.visibility = View.GONE
                    if (initiator != context?.getString(R.string.traveler_type)) {
                        cancelReason.visibility = View.VISIBLE
                        cancelReason.text = message
                    } else if (newMessageCount != 0) {
                        userDetails.messageContent.makeVisible()
                        userDetails.messageContent.text = message
                        userDetails.exclude.makeVisible()
                    }
                }
            }
        }
    }

    private fun buttonLogic(status: String?) {
        with(binding) {
            when (status) {
                Status.PENDING_PAYMENT.value -> {
                    confirmOrder.visibility = View.GONE
//                    btnConfirmOrderSticky.visibility = View.GONE
                    editOrder.visibility = View.VISIBLE
                    cancelOrder.visibility = View.VISIBLE
                    viewModel.showConfirmButton = false
                }
                Status.CANCELLED.value -> {
                    confirmOrder.visibility = View.GONE
//                    btnConfirmOrderSticky.visibility = View.GONE
                    editOrder.visibility = View.GONE
                    cancelOrder.visibility = View.GONE
                    viewModel.showConfirmButton = false

                }
                Status.PAID.value -> {
                    confirmOrder.visibility = View.GONE
//                    btnConfirmOrderSticky.visibility = View.GONE
                    editOrder.visibility = View.VISIBLE
                    cancelOrder.visibility = View.GONE
                    viewModel.showConfirmButton = false
                    viewModel.orderDetailsValue?.event?.date?.let {
                        if (it.isNotEmpty()) {
                            if (it.replace("-", "")
                                    .toInt() < SimpleDateFormat(
                                    "yyyy-MM-dd",
                                    Locale("ru")
                                ).format(Calendar.getInstance().time).replace("-", "").toInt()
                            )
                                editOrder.visibility = View.GONE
                        }
                    }
                }

                else -> {
                    editOrder.visibility = View.GONE
                    confirmOrder.visibility = View.VISIBLE
//                    btnConfirmOrderSticky.visibility = View.VISIBLE
                    cancelOrder.visibility = View.VISIBLE
                    viewModel.showConfirmButton = true
                }
            }
//            confirmButtonVisible()
        }

    }

    private fun navigateToConfirmOrEdit(isConfirm: Boolean) {
        navigateFragment(
            OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToConfirmOrderFragment(
                args.orderId,
                args.type,
                viewModel.orderDetailsValue?.experience?.title.toString(),
                isConfirm
            )
        )
    }

    private fun navigateToCancel(orderTitle: String) {
        navigateFragment(
            OrderDetailsIndividualFragmentDirections.actionOrderDetailsIndividualFragmentToOrderCancelFragment(
                args.orderId,
                orderTitle,
                StatisticsData(
                    statisticsData.ordersRateValue,
                    statisticsData.bookingRateValue,
                    statisticsData.confirmRateValue,
                    statisticsData.canOpenContacts
                ),
                viewModel.orderDetailsValue?.traveler?.id ?: 0,
                viewModel.orderDetailsValue?.status ?: "",
                args.type,
                viewModel.orderDetailsValue?.event?.awareStartDt ?: ""
            )
        )
    }

    override fun onPause() {
        super.onPause()
        viewModel.isNavigated = false
        viewModel.showConfirmButton = false
    }

    private fun discountLogic(discount: Double?, price: Double?, currency: String?) {
        with(binding) {
            if (discount != null && discount != 0.0) {
                val percentValue = (discount * 100).toInt()
                val salePriceValue = price?.div(1 - discount)?.toInt()
                percent.text = context?.getString(
                    R.string.discount_percent,
                    percentValue,
                    "%"
                )
                salePrice.text =
                    context?.getString(R.string.price_value)
                        ?.setParticipantAndCurrency(null, salePriceValue, currency?.currency())
                containerSale.makeVisible()
            } else {
                containerSale.makeVisibleGone()
            }
        }
    }

    private fun errorMessage(isOnline: Boolean) {
        with(binding) {
            activity?.setLightStatusBar()
            shimmer.layoutShimmer.makeVisibleGone()
            containerShimmer.makeVisibleGone()
            containerError.makeVisible()

            error.title.text =
                if (isOnline) context?.getStringRes(R.string.call_error_title) else context?.getString(
                    R.string.no_internet_title
                )
            error.message.text =
                if (isOnline) context?.getStringRes(R.string.call_error_message) else context?.getString(
                    R.string.no_internet_message
                )
        }
    }

    private fun isOnlineAgain() {
        with(binding) {
            if (context?.isOnline() == true) {
                activity?.setFullScreenAndLightStatusBar()
                containerError.makeVisibleGone()
                containerShimmer.makeVisible()
                shimmer.layoutShimmer.makeVisible()
                viewModel.getOrderDetails(args.orderId)
            }
        }
    }

    private fun setProgress() {
        binding.motionLayout.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (isAdded) {
                    with(binding) {
                        motionLayout.progress = viewModel.currentProgress
                        viewModel.titleHeight = titleTv.height
//                    if (viewModel.currentProgress == 1.0f) {
//                        nestedScrollView.smoothScrollTo(0, binding.nestedScrollView.bottom)
//                    }
                    }
                    binding.motionLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    private fun setMarginToMotionLayoutItem(constraintId: Int) {
        binding.root.getConstraintSet(constraintId)
            .setMargin(
                binding.toolbar.id,
                ConstraintSet.TOP,
                (activity as MainActivity).statusBarHeight()
            )
    }


//    private fun confirmButtonVisible() {
//        with(binding) {
//            nestedScrollView.viewTreeObserver.addOnGlobalLayoutListener(object :
//                ViewTreeObserver.OnGlobalLayoutListener {
//                override fun onGlobalLayout() {
//                    if (isAdded) {
//                        nestedScrollView.viewTreeObserver.removeOnGlobalLayoutListener(this)
//                        val bottomY =
//                            confirmOrder.getLocationOnScreen().y - confirmOrder.measuredHeight
//                        val stickyButtonY =
//                            btnConfirmOrderSticky.getLocationOnScreen().y - btnConfirmOrderSticky.measuredHeight
//                        if (viewModel.showConfirmButton) {
//                            if (bottomY < stickyButtonY) {
//                                btnConfirmOrderSticky.visibility = View.GONE
//                                confirmOrder.visibility = View.VISIBLE
//
//                            } else {
//                                clConfirmOrderSticky.visibility = View.VISIBLE
//                                btnConfirmOrderSticky.visibility = View.VISIBLE
//                                confirmOrder.visibility = View.GONE
//                            }
//                        }
//                    }
//                }
//            })
//        }
//    }

    companion object {
        const val IS_MODIFIED_KEY = "IS_MODIFIED_KEY"
        const val IS_MODIFIED_BUNDLE = "IS_MODIFIED_BUNDLE"
    }
}
