package ru.tripster.guide.ui.fragments.orderDetails.groupTour

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.events.OrderDetailsForChat
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.domain.model.events.Status
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.guide.R
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_BACK_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_CALL_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_CONFIRM_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_CONTACT_SUPPORT
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_EVENT_EDIT
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_REGISTRATION_CLOSE_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.GROUP_EXP_TRAVELLER_NAME
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.BottomSheetCloseBookingBinding
import ru.tripster.guide.databinding.FragmentOrderDetailsGroupBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment
import ru.tripster.guide.ui.fragments.confirmOrder.ConfirmOrEditOrderFragment
import ru.tripster.guide.ui.fragments.confirmOrder.EditPriceBottomSheet.Companion.EDIT_AMOUNT_KEY
import ru.tripster.guide.ui.fragments.confirmOrder.EditPriceBottomSheet.Companion.NEW_AVAILABLE_AMOUNT_KEY
import ru.tripster.guide.ui.fragments.confirmOrder.EditPriceBottomSheet.Companion.ORDER_GROUP


class OrderDetailsGroupTourFragment :
    FragmentBaseNCMVVM<OrderDetailsGroupTourViewModel, FragmentOrderDetailsGroupBinding>() {
    override val viewModel: OrderDetailsGroupTourViewModel by viewModel()
    override val binding: FragmentOrderDetailsGroupBinding by viewBinding()
    private val args: OrderDetailsGroupTourFragmentArgs by navArgs()
    private lateinit var orderDetailsType: String
    private val toasts = mutableListOf<Toast>()
    private lateinit var statisticData: StatisticsData

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (activity as MainActivity).transparentStatusBar()
        activity?.bottomNavBarVisibility(true)

        activity?.onBackPressedDispatcher?.addCallback(this) {
            if (args.isNavigatedDateOrder) {
                navigateFragment(OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupTourFragmentToOrderFragment())
            } else popBackStack()
        }
    }

    override fun onView() {
        activity?.bottomNavBarVisibility(true)
        (activity as MainActivity).transparentStatusBar()

        setMarginToMotionLayoutItem(R.id.start)
        setMarginToMotionLayoutItem(R.id.end)

        if (viewModel.currentProgress == 1.0f) {
            binding.toolbar.alpha = 1.0f
            activity?.setFullScreenAndLightStatusBar()
        }

        statisticData = Gson().fromJson(args.statisticData, StatisticsData::class.java)
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

                    divider.alpha = 0f
                    when (motionLayout.progress) {
                        in 0.0f..0.75f -> {
                            setMarginToMotionLayoutItem(R.id.start)
                            statusOfBooking.makeVisible()
                            titleTv.makeVisible()
                            toolbar.alpha = 0f
                            (activity as MainActivity).transparentStatusBar()
                            activity?.setStatusBarWhiteTextFullScreen()
                            viewModel.currentProgress = 0.0f
                        }
                        in 0.75f..1.0f -> {
                            setMarginToMotionLayoutItem(R.id.end)
                            statusOfBooking.makeVisibleGone()
                            titleTv.makeVisibleGone()
                            toolbar.alpha = motionLayout.progress
                            activity?.setFullScreenAndLightStatusBar()
                            viewModel.currentProgress = 1.0f
                            motionLayout.transitionToEnd()
                        }
                    }

                    viewModel.orderDetailsGroup?.status?.statusTypeGroupTour(
                        statusOfBooking,
                        context,
                        viewModel.orderDetailsGroup?.awareStartDt ?: "",
                        motionLayout.progress
                    )
                }

                override fun onTransitionCompleted(motionLayout: MotionLayout, i: Int) {
                    setProgress()
                    divider.alpha = if (motionLayout.progress == 1f) 1f else 0f
                }

                override fun onTransitionTrigger(
                    motionLayout: MotionLayout,
                    i: Int,
                    b: Boolean,
                    v: Float
                ) {
                }
            })
        }

        if (context?.isOnline() == true) {
            viewModel.setNewOrderDetails(args.orderId, getCurrentDateAndTime())
        } else {
            errorMessage(false)
        }

        if (viewModel.currentProgress == 1.0f) {
            activity?.setFullScreenAndLightStatusBar()
        }

        setProgress()
        setFragmentResultListener(
            ConfirmOrEditOrderFragment.CONFIRM_ORDER_REQUEST_KEY
        )
        { _, bundle ->
            val result =
                bundle.getParcelable<OrderDetails>(ConfirmOrEditOrderFragment.CONFIRM_ORDER_BUNDLE)
            context?.getString(R.string.order_confirmed)
                ?.let { Toast(context).showCustomToast(it, this, true) }
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
        viewModel.setNewOrderDetails(args.orderId, getCurrentDateAndTime())
    }

    override fun onEach() {
        onEach(viewModel.perOrderDetails) { order ->
            viewModel.perOrderValue = order
        }

        onEach(viewModel.orderDetails) {
            it?.let { orderDetails ->
                viewModel.orderDetailsGroup = orderDetails
                val date = orderDetails.date?.dropLast(12)
                orderDetailsType = orderDetails.type ?: ""

                with(binding) {
                    toolbar.setTitle(orderDetails.title ?: "")
                    toolbar.setStatusGroupTour(
                        orderDetails.status ?: "",
                        orderDetails.awareStartDt
                    )

                    containerAllDetails.makeVisible()

                    if (orderDetails.type == context?.getString(R.string.group_type)) {
                        tourHourDate.text = orderDetails.date?.takeLast(11)
                        tourDateDuration.text = date
                    } else {
                        val days = orderDetails.duration?.let { duration ->
                            duration.toInt().div(24) + 1
                        }
                        tourHourDate.text = date
                        tourDateDuration.text = context?.getString(R.string.tour_days, days)
                    }

                    orderDetails.status?.statusTypeGroupTour(
                        statusOfBooking,
                        context,
                        orderDetails.awareStartDt ?: "",
                        viewModel.currentProgress
                    )

                    titleTv.text = orderDetails.title
                    availablePlaces.text = context?.getString(
                        R.string.available_places_group,
                        orderDetails.numberOfPersonsAvail,
                        orderDetails.maxPersons
                    )

                    context?.let { context ->
                        Glide.with(context).load(orderDetails.coverImage)
                            .centerCrop()
                            .into(ivToolbarImage)
                    }

                    orderDetails.orders?.let { orders ->
                        viewModel.orderList = emptyList()

                        viewModel.orderList =
                            orders.pendingPayment + orders.paid + orders.confirmation + orders.cancelled + orders.messaging

//                        when {
//                            orders.paid.isNotEmpty() -> viewModel.orderList + orders.paid
//
//                            orders.pendingPayment.isNotEmpty() -> viewModel.orderList + orders.pendingPayment
//
//                            orders.confirmation.isNotEmpty() -> orders.confirmation[0]
//                                .let { item ->
//                                    viewModel.orderList.add(item)
//                                }
//                            orders.cancelled.isNotEmpty() -> orders.cancelled[0]
//                                .let { item ->
//                                    viewModel.orderList.add(item)
//                                }
//                            orders.messaging.isNotEmpty() -> orders.messaging[0].let { item ->
//                                viewModel.orderList.add(item)
//                            }
//                        }

                        orderDetails.currency?.currency()
                            ?.let { currency ->
                                orderDetails.type?.let { type ->
                                    touristItemInGroup.setData(
                                        orders, currency,
                                        type, orderDetails.awareStartDt ?: "",
                                        orderDetails.numberOfPersonsPaid
                                    )
                                }
                            }
                    }

                    orderDetails.status?.let { status ->
                        buttonLogic(
                            status,
                            orderDetails.awareStartDt ?: ""
                        )
                    }
                    touristItemInGroup.ordersRateValue = statisticData.ordersRateValue
                }

                if (viewModel.isFromCloseBooking != null) {
                    showToast(viewModel.isFromCloseBooking ?: false, it.numberOfPersonsAvail)
                }
            }
            shimmerVisibility()

        }

        onEach(viewModel.orderDetailsError) {
            errorMessage(true)
        }
    }

    override fun onViewClick() {
        with(binding) {

            error.update.setOnClickListener {
                isOnlineAgain()
            }

            contactSupport.setOnClickListener {

                if (!viewModel.isNavigated) {
                    context?.let {
                        viewModel.orderDetailsGroupTourClicked(
                            it,
                            GROUP_EXP_CONTACT_SUPPORT,
                            args.orderId
                        )
                    }

                    context?.let { context ->
                        this@OrderDetailsGroupTourFragment.contactSupportBottomSheet(
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

            touristItemInGroup.setOnPhoneClickListener { status, phoneNumber, touristId, orderNumber ->
                context?.let {
                    viewModel.orderDetailsGroupTourClicked(
                        it,
                        GROUP_EXP_CALL_ICON,
                        args.orderId,
                        orderNumber,
                        touristId
                    )
                }

                if (status != Status.PAID.value) {
                    navigateFragment(
                        OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupTourFragmentToSeeContactWarningBottomSheet(
                            statisticData,
                            context?.getString(R.string.group_type) ?: ""
                        )
                    )
                } else {
                    context?.let { phoneNumber.phoneCall(it) }
                }
            }

            backButton.setOnClickListener {
                context?.let {
                    viewModel.orderDetailsGroupTourClicked(it, GROUP_EXP_BACK_ICON, args.orderId)
                }

                if (args.isNavigatedDateOrder) {
                    navigateFragment(OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupTourFragmentToOrderFragment())
                } else popBackStack()
            }

            touristItemInGroup.setOnUserItemClickListener { id, touristId, orderNumber ->
                context?.let { context ->
                    viewModel.orderDetailsGroupTourClicked(
                        context,
                        GROUP_EXP_TRAVELLER_NAME,
                        args.orderId,
                        orderNumber,
                        touristId
                    )
                }

                navigateFragment(
                    OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupFragmentToUserOrderGroupDetailsFragment(
                        id, orderDetailsType, StatisticsData(
                            statisticData.ordersRateValue,
                            statisticData.bookingRateValue,
                            statisticData.confirmRateValue,
                            statisticData.canOpenContacts
                        )
                    )
                )
            }

            touristItemInGroup.setOnConfirmClickListener { id, touristId, orderNumber ->
                context?.let { context ->
                    viewModel.orderDetailsGroupTourClicked(
                        context,
                        GROUP_EXP_CONFIRM_BUTTON,
                        args.orderId,
                        orderNumber,
                        touristId
                    )
                }

                viewModel.getOrderDetails(id)

                if (viewModel.perOrderValue != null) {
                    navigateFragment(
                        OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupFragmentToConfirmOrderFragment(
                            viewModel.perOrderValue?.id ?: 0,
                            orderDetailsType,
                            viewModel.perOrderValue?.experience?.title ?: "",
                            true
                        )
                    )
                }
            }
            touristItemInGroup.setOnMessageClickListener { id, status, details, traveler, amplitudeId, orderNumber ->
                context?.let {
                    viewModel.orderDetailsGroupTourClicked(
                        it,
                        amplitudeId,
                        args.orderId,
                        orderNumber,
                        traveler.id
                    )
                }

                navigateFragment(
                    OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupTourFragmentToChatFragment(
                        Gson().toJson(
                            OrderDetailsForChat(
                                id,
                                status,
                                viewModel.orderDetailsGroup?.title ?: "",
                                context?.getString(R.string.group_type) ?: "",
                                viewModel.orderDetailsGroup?.date ?: "",
                                false,
                                traveler.phone,
                                traveler.name,
                                traveler.avatar150Url,
                                details, viewModel.orderDetailsGroup?.currency ?: "",
                                true
                            )
                        ), context?.getString(R.string.group_type) ?: "",
                        Gson().toJson(
                            StatisticsData(
                                statisticData.ordersRateValue,
                                statisticData.bookingRateValue,
                                statisticData.confirmRateValue,
                                statisticData.canOpenContacts
                            )
                        )
                    )
                )
            }

            containerAllDetails.setOnClickListener {
                context?.let {
                    viewModel.orderDetailsGroupTourClicked(it, GROUP_EXP_EVENT_EDIT, args.orderId)
                }

                navigateFragment(
                    OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupTourFragmentToEditPriceBottomSheet(
                        "",
                        "",
                        ORDER_GROUP,
                        viewModel.orderDetails.value?.numberOfPersonsAvail.toString(),
                        viewModel.currentProgress
                    )
                )
                viewModel.isFromCloseBooking = false
            }
            openBooking.setOnClickListener {
                navigateFragment(
                    OrderDetailsGroupTourFragmentDirections.actionOrderDetailsGroupTourFragmentToEditPriceBottomSheet(
                        "",
                        "",
                        ORDER_GROUP,
                        viewModel.orderDetails.value?.numberOfPersonsAvail.toString(),
                        viewModel.currentProgress
                    )
                )
                viewModel.isFromCloseBooking = false
            }
            closeBooking.setOnClickListener {
                closeBookingDialog()
            }
        }
    }

    private fun closeBookingDialog() {
        val dialog = context?.let { context -> BottomSheetDialog(context) }
        val dialogBinding =
            BottomSheetCloseBookingBinding.inflate(LayoutInflater.from(context))

        dialogBinding.closeBooking.setOnClickListener {
            context?.let {
                viewModel.orderDetailsGroupTourClicked(
                    it,
                    GROUP_EXP_REGISTRATION_CLOSE_BUTTON,
                    args.orderId
                )
            }
            viewModel.setNewAvailableSpaceAmount(
                args.orderId, 0,
                getCurrentDateAndTime()
            )

            viewModel.isFromCloseBooking = true
            dialog?.dismiss()
        }

        statusBarTypeDialog(dialog)

        dialogBinding.cancel.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.let {
            it.setContentView(dialogBinding.root)
            it.show()
        }
    }

    private fun showToast(isClosed: Boolean, count: Int? = null) {
        cancelAllToasts()

        if (isClosed || count == 0) {
            context?.getString(R.string.order_canceled_toast)
                ?.let {
                    Toast(context).showCustomToast(it, this, true)
                }.apply {
                    this?.let { toasts.add(it) }
                }
        } else {
            context?.getString(
                R.string.available_places_toast_text,
                count,
                count?.availablePlacesRightForm()
            )
                ?.let {
                    Toast(context).showCustomToast(it, this, true)
                }.apply {
                    this?.let { toasts.add(it) }
                }
        }
    }

    private fun cancelAllToasts() {
        toasts.forEach {
            it.cancel()
        }
        toasts.clear()
    }

    override fun onResume() {
        super.onResume()
        setFragmentResultListener(EDIT_AMOUNT_KEY) { _, bundle ->
            val addedPlaces = bundle.getString(NEW_AVAILABLE_AMOUNT_KEY)?.toInt() ?: 0
            if (bundle.getString(NEW_AVAILABLE_AMOUNT_KEY)?.toInt() == 0) {
                closeBookingDialog()
            } else {
                viewModel.setNewAvailableSpaceAmount(
                    args.orderId,
                    addedPlaces,
                    getCurrentDateAndTime()
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.isFromCloseBooking = null
    }

    private fun buttonLogic(status: String, awareStartDt: String) {
        with(binding) {
            when (status) {
                Status.PENDING_PAYMENT.value -> {
                    closeBooking.isVisible = true
                    openBooking.isVisible = false
                }
                Status.PAID.value -> {
                    openBooking.isVisible = !awareStartDt.check24Hours()
                    closeBooking.isVisible = false
                }
                Status.CANCELLED.value -> {
                    closeBooking.isVisible = false
                    openBooking.isVisible = false
                }
            }
        }
    }

    private fun statusBarTypeDialog(dialog: BottomSheetDialog?) {
        when (viewModel.currentProgress) {
            in 0.0f..0.75f -> {
                dialog?.window?.decorView?.systemUiVisibility =
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            }
            in 0.75f..1.0f -> {
                dialog.setFullScreenAndLightStatusBarDialog()
            }
        }
    }

    private fun shimmerVisibility() {
        with(binding) {
            containerButton.makeVisible()
            containerShimmer.isClickable = false
            containerShimmer.isFocusable = false

            shimmer.layoutShimmer.makeVisibleGone()
            containerShimmer.makeVisibleGone()
            motionLayout.makeVisible()

            val vto: ViewTreeObserver = binding.containerDetails.viewTreeObserver
            vto.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    if (isAdded) {
                        binding.containerDetails.viewTreeObserver.removeOnGlobalLayoutListener(this)
                        val height: Int = binding.containerDetails.measuredHeight

                        context?.let {
                            nestedScrollView.isNestedScrollingEnabled =
                                height + ivToolbarImage.measuredHeight - it.dpToPx(R.dimen.space_16) >= containerShimmer.measuredHeight
                        }
                    }
                }
            })
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
                viewModel.setNewOrderDetails(args.orderId, getCurrentDateAndTime())
            }
        }
    }

    private fun setMarginToMotionLayoutItem(constraintId: Int) {
        binding.root.getConstraintSet(constraintId)
            .setMargin(binding.toolbar.id, ConstraintSet.TOP, (activity as MainActivity).statusBarHeight())
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
}