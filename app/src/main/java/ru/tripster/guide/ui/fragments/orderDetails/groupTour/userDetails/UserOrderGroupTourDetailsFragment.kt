package ru.tripster.guide.ui.fragments.orderDetails.groupTour.userDetails

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
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
import ru.tripster.guide.R
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_ORDER_BACK_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_ORDER_CALL_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_ORDER_CHAT_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_ORDER_CHAT_PREVIEW
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_ORDER_CONTACT_SUPPORT
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_ORDER_GET_CONTACT_TIP
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_ORDER_ORDER_CANCEL
import ru.tripster.guide.Screen
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentUserOrderDetailsGroupTourBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment

class UserOrderGroupTourDetailsFragment :
    FragmentBaseNCMVVM<UserOrderGroupTourDetailsViewModel, FragmentUserOrderDetailsGroupTourBinding>() {
    override val viewModel: UserOrderGroupTourDetailsViewModel by viewModel()
    override val binding: FragmentUserOrderDetailsGroupTourBinding by viewBinding()
    private val args: UserOrderGroupTourDetailsFragmentArgs by navArgs()
    private lateinit var orderDetailsValue: OrderDetails
    private var booleanOfContacts = false

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            popBackStack()
        }
    }
    override fun onView() {
        activity?.setLightStatusBar()
        activity?.bottomNavBarVisibility(true)
        binding.shimmer.clToolbarContainer.makeVisibleGone()

        if (context?.isOnline() == true)
            viewModel.getOrderDetails(args.userOrderId) else {
            errorMessage(false)
        }

        binding.toolbar.backButtonClicked {
            context?.let {
                viewModel.experienceClicked(it, GROUP_ORDER_BACK_ICON, args.type)
            }

            popBackStack()
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            MainFragment.AMPLITUDE_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getInt(MainFragment.AMPLITUDE_BUNDLE)
            context?.let {
                context?.let { context -> viewModel.menuItemClicked(context, result.menuItem()) }
            }
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.getOrderDetails(args.userOrderId)
    }

    override fun onViewClick() {
        with(binding) {
            confirmOrder.setOnClickListener {
                navigateToConfirm()
            }

            error.update.setOnClickListener {
                isOnlineAgain()
            }

            contactSupport.setOnClickListener {
                context?.let {
                    viewModel.experienceClicked(it, GROUP_ORDER_CONTACT_SUPPORT, args.type)
                }

                context?.let { context ->
                    this@UserOrderGroupTourDetailsFragment.contactSupportBottomSheet(
                        context, args.userOrderId, isFromProfile = false, onNavigated = {
                            lifecycleScope.launch {
                                delay(1500)
                                it()
                            }
                        }
                    ) {}
                }
            }

            userDetails.phone.setOnClickListener {
                context?.let {
                    viewModel.experienceClicked(it, GROUP_ORDER_CALL_ICON, args.type)
                }

                if (orderDetailsValue.status != Status.PAID.value) {
                    navigateFragment(
                        UserOrderGroupTourDetailsFragmentDirections.actionUserOrderGroupTourDetailsFragmentToSeeContactWarningBottomSheet(
                            args.statisticData,
                            context?.getString(R.string.tour_phone_type) ?: ""
                        )
                    )
                } else {
                    context?.let { context ->
                        orderDetailsValue.traveler.phone.phoneCall(context)
                    }
                }
            }
            cancelOrder.setOnClickListener {
                context?.let {
                    viewModel.experienceClicked(it, GROUP_ORDER_ORDER_CANCEL, args.type)
                }

                orderDetailsValue.experience.title.let {
                    navigateToCancel(it)
                }
            }
            userDetails.messageContent.setOnClickListener {
                context?.let {
                    viewModel.experienceClicked(it, GROUP_ORDER_CHAT_PREVIEW, args.type)
                }

                navigateToChat()
            }

            userDetails.message.setOnClickListener {

                context?.let {
                    viewModel.experienceClicked(it, GROUP_ORDER_CHAT_ICON, args.type)
                }

                navigateToChat()
            }

//            cancelOrder.setOnClickListener {
//                orderDetailsValue.experience.title.let {
////                    navigateToCancel(it)
//                }
//            }
            phoneNumber.setOnClickListener {
                context?.let { context -> orderDetailsValue.traveler.phone.phoneCall(context) }
            }

            emailAddress.setOnClickListener {
                startActivity(
                    orderDetailsValue.traveler.email.openMail()
                )
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.orderDetails) {
            it?.let { orderDetails ->
                viewModel.details = orderDetails
                orderDetailsValue = orderDetails
                with(binding.toolbar) {
                    setNumber(orderDetails.id)
                    setTitle(orderDetails.experience.title)
                    setStatus(
                        orderDetails.status,
                        orderDetails.event.awareStartDt,
                        binding.checkIcon,
                        orderDetails.reject.initiator
                    )
                }

                with(binding.userDetails) {
                    context?.let { context ->
                        if (orderDetails.traveler.avatar150Url.isNotEmpty()) {
                            avatar.setStrokeColorResource(R.color.ten_percent_of_black)
                            avatar.strokeWidth = 2f
                        }
                        Glide.with(context).load(orderDetails.traveler.avatar150Url)
                            .placeholder(R.drawable.ic_avatar_icon_placeholder)
                            .centerCrop()
                            .into(avatar)
                    }

                    name.text = orderDetails.traveler.name.shortedSurname()
                    details.text = context?.getStringRes(R.string.traveler)

                    messageLogic(orderDetails.lastMessage.comment)
                    messageCountLogic(orderDetails.newMessagesCount)
                    dateAndHourLogic(
                        orderDetails.event.date,
                        orderDetails.event.time,
                        orderDetails.experience.duration
                    )
                    peopleCountLogic(orderDetails.personsCount, orderDetails.price.perPicket)
                    priceLogic(
                        orderDetails.price.value.toInt(),
                        orderDetails.price.prePay.toInt(),
                        orderDetails.price.paymentToGuide.toInt(),
                        orderDetails.price.currency.currency()
                    )
                    meetingPlace(orderDetails.event.meetingPoint.description)

                    if (orderDetails.event.awareStartDt.check24Hours() && orderDetails.status == Status.PAID.value) {
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
                        contactLogic(orderDetails.traveler.email, orderDetails.traveler.phone)
                    }

                    rejectLogic(
                        orderDetails.newMessagesCount,
                        orderDetails.reject.initiator,
                        orderDetails.reject.reason_text,
                        orderDetails.reject.message
                    )
                    discountLogic(
                        orderDetails.discountRate,
                        orderDetails.price.value,
                        orderDetails.price.currency.currency()
                    )
                    buttonLogic(orderDetails.status)
                    shimmerVisibility()
                }
            }
        }

        onEach(viewModel.orderDetailsError) {
            errorMessage(true)
        }
    }

    private fun shimmerVisibility() {
        with(binding) {
            containerShimmer.makeVisibleGone()
            shimmer.layoutShimmer.makeVisibleGone()
            containerShimmer.isClickable = false
            containerShimmer.isFocusable = false
        }
    }

    private fun navigateToChat() {
        navigateFragment(
            UserOrderGroupTourDetailsFragmentDirections.actionUserOrderGroupTourDetailsFragmentToChatFragment(
                Gson().toJson(
                    OrderDetailsForChat(
                        orderDetailsValue.id,
                        orderDetailsValue.status,
                        orderDetailsValue.experience.title,
                        context?.getString(R.string.group_type) ?: "",
                        binding.tourDate.text.toString() + " " + binding.tourHour.text,
                        false,
                        orderDetailsValue.traveler.phone,
                        orderDetailsValue.traveler.name,
                        orderDetailsValue.traveler.avatar150Url,
                        context?.getString(
                            R.string.user_details_chat,
                            binding.peopleText.text.toString(),
                            binding.paymentText.text.toString()
                        ) ?: "",
                        orderDetailsValue.price.currency,
                        orderDetailsValue.newMessagesCount != 0
                    )
                ), context?.getString(R.string.tour_type) ?: "",
                Gson().toJson(
                    StatisticsData(
                        args.statisticData.ordersRateValue,
                        args.statisticData.bookingRateValue,
                        args.statisticData.confirmRateValue,
                        args.statisticData.canOpenContacts
                    )
                )
            )
        )
    }

    private fun messageLogic(message: String?) {
        with(binding.userDetails) {
            if (!message.isNullOrEmpty()) {
                val listOfWord = message.split(" ").toMutableList()
                listOfWord.forEachIndexed { index, word ->
                    word.replace("-", "")
                    if (checkPhone(word)) {
                        hideContacts(listOfWord, word, index, true).let {
                            messageContent.setResizableText(
                                it,
                                3,
                                true
                            )
                        }
                        messageContent.makeVisible()
                        exclude.makeVisible()
                    } else {
                        hideContacts(listOfWord, word, index, false).let {
                            messageContent.setResizableText(
                                it,
                                3,
                                true
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

    private fun checkPhone(phoneNumber: String): Boolean =
        Patterns.PHONE.matcher(phoneNumber)
            .matches() && phoneNumber.length >= 10

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

    private fun dateAndHourLogic(date: String?, hour: String?, duration: Double?) {
        with(binding) {
            if (!date.isNullOrEmpty()) {
                tourDate.text = date.dateFormatting()
                tourHour.visibility = View.VISIBLE
                noDateInfo.visibility = View.GONE
                hourLogic(hour, duration, date)
            } else {
                guidelineForDateInfo.setGuidelinePercent(0.8F)
                tourDate.text = context?.getStringRes(R.string.no_date_and_time)
                noDateInfo.visibility = View.VISIBLE
                noDateInfo.text = context?.getStringRes(R.string.no_date_and_time_info)
                tourHour.visibility = View.GONE
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
            set.clone(containerDetails)

            if (count != null && count != 0) {
                containerPeopleInfo.visibility = View.VISIBLE
                peopleText.text = resources.getQuantityString(
                    R.plurals.people_count_tour, count, count
                )

                paramsContainerPeople.width = 0
                containerPeople.gravity = Gravity.CENTER or Gravity.START
                containerPeople.layoutParams = paramsContainerPeople

                set.connect(
                    containerPeople.id, ConstraintSet.END,
                    guideline.id, ConstraintSet.START
                )

                if (!perTicket.isNullOrEmpty()) {
                    containerPeopleInfo.visibility = View.VISIBLE
                    ticket.visibility = View.GONE
                    perTicket.forEach {
                        val type = context?.let { context -> AppCompatTextView(context) }
                        type?.setTextAppearance(R.style.Text_17_400)
                        type?.text = context?.getString(R.string.ticket_type, it.count, it.title)
                        containerPeopleInfo.addView(type)
                    }
                }
            } else {
                containerPeopleInfo.visibility = View.INVISIBLE
                ticket.visibility = View.INVISIBLE
                guidelineForPeopleInfo.setGuidelinePercent(0.8F)
                guidelineForPeopleInfo.setGuidelinePercent(0.8F)

                paramsContainerPeople.width = LinearLayout.LayoutParams.WRAP_CONTENT

                containerPeople.gravity = Gravity.CENTER
                containerPeople.layoutParams = paramsContainerPeople

                set.clear(
                    containerPeople.id, ConstraintSet.END
                )

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
                        ?.setParticipantAndCurrency(null, value, currency?.currency())
                priceTripster.text =
                    context?.getString(R.string.price_on_tripster)
                        ?.setTripsterCurrency(prePay, currency?.currency())
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
                    meetingPlaceInfo.setResizableText(
                        it,
                        7,
                        viewMore = true,
                        isViewMoreClickable = true
                    )
                }
                meetingPlace.visibility = View.VISIBLE
                meetingPlaceInfo.visibility = View.VISIBLE
            } else {
                meetingPlace.visibility = View.GONE
                meetingPlaceInfo.visibility = View.GONE
            }

        }
    }

    private fun discountLogic(discount: Double?, price: Double?, currency: String?) {
        with(binding) {
            if (discount != null && discount != 0.0) {
                val percentValue = (discount * 100).toInt()
                val salePriceValue = price?.plus((price * percentValue) / 100)?.toInt()
                percent.text = context?.getString(R.string.discount_percent, percentValue, "%")
                salePrice.text =
                    context?.getString(R.string.price_value)
                        ?.setParticipantAndCurrency(null, salePriceValue, currency?.currency())
                containerSale.visibility = View.VISIBLE
            } else {
                containerSale.visibility = View.GONE
            }
        }

    }

    private fun contactLogic(email: String?, phone: String?) {
        with(binding) {
            if (!email.isNullOrEmpty() && !phone.isNullOrEmpty()) {
                if (orderDetailsValue.status == Status.PAID.value) {
                    phoneNumber.text = phone
                    emailAddress.text = email
                    phoneNumber.makeVisible()
                    emailAddress.makeVisible()
                    userContactsInfo.makeVisibleGone()
                } else {
                    userContactsInfo.makeVisible()
                    phoneNumber.makeVisibleGone()
                    emailAddress.makeVisibleGone()
                    userContactsInfo.text = context?.getStringRes(R.string.user_contact_info)
                    context?.let { context ->
                        userContactsInfo.initTextLinks(
                            resources.getString(R.string.user_contact_info),
                            context,
                            resources.getString(R.string.user_contact_info_colored)
                        ) {
                            viewModel.experienceClicked(
                                context,
                                GROUP_ORDER_GET_CONTACT_TIP,
                                args.type
                            )

                            navigateFragment(
                                UserOrderGroupTourDetailsFragmentDirections.actionUserOrderGroupTourDetailsFragmentToSeeContactWarningBottomSheet(
                                    args.statisticData,
                                    context.getString(R.string.tour_type) ?: ""
                                )
                            )
                        }
                    }
                }
            } else if (orderDetailsValue.status == Status.CANCELLED.value) {
                phoneNumber.makeVisibleGone()
                emailAddress.makeVisibleGone()
                userContactsInfo.makeVisible()
                userDetails.phone.makeVisibleGone()
                userContactsInfo.text = context?.getString(R.string.info_details_cancelled_ended)
            } else {
                userContactsInfo.makeVisible()
                phoneNumber.makeVisibleGone()
                emailAddress.makeVisibleGone()
                userDetails.phone.makeVisible()
                userContactsInfo.text = context?.getStringRes(R.string.user_contact_info)
                context?.let { context ->
                    userContactsInfo.initTextLinks(
                        resources.getString(R.string.user_contact_info),
                        context,
                        resources.getString(R.string.user_contact_info_colored)
                    ) {
                        navigateFragment(
                            UserOrderGroupTourDetailsFragmentDirections.actionUserOrderGroupTourDetailsFragmentToSeeContactWarningBottomSheet(
                                args.statisticData,
                                context.getString(R.string.tour_type) ?: ""
                            )
                        )
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
                    cancelOrder.visibility = View.VISIBLE
                }
                Status.CANCELLED.value, Status.PAID.value -> {
                    confirmOrder.visibility = View.GONE
                    cancelOrder.visibility = View.GONE
                }

                Status.PAID.value -> {
                    confirmOrder.visibility = View.GONE
                    cancelOrder.visibility = View.GONE
                }

                else -> {
                    confirmOrder.visibility = View.VISIBLE
                    cancelOrder.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun navigateToConfirm() {
        navigateFragment(
            UserOrderGroupTourDetailsFragmentDirections.actionUserOrderGroupTourDetailsFragmentToConfirmOrderFragment(
                args.userOrderId, args.type, orderDetailsValue.experience.title, true
            )
        )
    }

    private fun rejectLogic(
        newMessageCount: Int,
        initiator: String,
        reason: String?,
        message: String?
    ) {
        with(binding) {
            when {
                reason.isNullOrEmpty() && message.isNullOrEmpty() -> {
                    containerCancel.visibility =
                        View.GONE
                    userDetailsContainer.setMargins(null, R.dimen.space_0, null, null)
                }
                !reason.isNullOrEmpty() && !message.isNullOrEmpty() -> {
                    containerCancel.visibility = View.VISIBLE
                    userDetailsContainer.setMargins(null, R.dimen.space_24, null, null)
                    cancelReasonTitle.visibility = View.VISIBLE
                    cancelReasonTitle.text = reason
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
                    userDetailsContainer.setMargins(null, R.dimen.space_24, null, null)
                    cancelReason.visibility = View.GONE
                    cancelReasonTitle.text = reason
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

    private fun navigateToCancel(orderTitle: String) {
        navigateFragment(
            UserOrderGroupTourDetailsFragmentDirections.actionUserOrderGroupTourDetailsFragmentToOrderCancelFragment(
                args.userOrderId,
                orderTitle,
                StatisticsData(
                    args.statisticData.ordersRateValue,
                    args.statisticData.bookingRateValue,
                    args.statisticData.confirmRateValue,
                    args.statisticData.canOpenContacts
                ),
                viewModel.details?.traveler?.id ?: 0,
                viewModel.details?.status ?: "",
                args.type,
                viewModel.details?.event?.awareStartDt ?: ""
            )
        )
    }

    private fun isOnlineAgain() {
        with(binding) {
            if (context?.isOnline() == true) {
                containerError.makeVisibleGone()
                containerShimmer.makeVisible()
                shimmer.layoutShimmer.makeVisible()
                containerError.makeVisibleGone()
                viewModel.getOrderDetails(args.userOrderId)
            }
        }
    }

    private fun errorMessage(isOnline: Boolean) {
        with(binding) {
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
}
