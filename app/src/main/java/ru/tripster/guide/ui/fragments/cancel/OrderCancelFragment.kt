package ru.tripster.guide.ui.fragments.cancel

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.cancel.Data
import ru.tripster.domain.model.cancel.OrderCancelData
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.guide.R
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentOrderCancelBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainFragment

import ru.tripster.guide.ui.calendar.CalendarBottomSheet

class OrderCancelFragment : FragmentBaseNCMVVM<OrderCancelViewModel, FragmentOrderCancelBinding>() {
    override val viewModel: OrderCancelViewModel by viewModel()
    override val binding: FragmentOrderCancelBinding by viewBinding()
    private val args: OrderCancelFragmentArgs by navArgs()


    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            popBackStack()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onView() {
        activity?.setLightStatusBar()
        activity?.bottomNavBarVisibility(false)

        with(binding) {
            context?.getString(
                R.string.order_number_and_place,
                args.orderId,
                args.orderTitle
            )?.let { toolbar.setSubTitle(it) }

            lConst.viewTreeObserver.addOnGlobalLayoutListener {
                if (isAdded) {
                    val rect = Rect()
                    lConst.getWindowVisibleDisplayFrame(rect)
                    val screenHeight: Int = lConst.rootView.height
                    val keypadHeight = screenHeight - rect.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        commentEdt.setBackgroundResource(R.drawable.shape_stroke_orange_80)
                        if (Build.VERSION.SDK_INT <= 28) commentEdt.text?.let {
                            commentEdt.setSelection(
                                it.length
                            )
                        }
                        commentEdt.isCursorVisible = true
                    } else {
                        commentEdt.setBackgroundResource(R.drawable.shape_stroke_gray)
                        commentEdt.isCursorVisible = false
                    }
                }
            }
            setFragmentResultListener(CalendarBottomSheet.CLOSED_DAY_BUNDLE) { _, bundle ->
                viewModel.selectedDate =
                    bundle.getString(CalendarBottomSheet.CLOSED_DAY_REQUEST_KEY) ?: ""
                radioGroupContainer.setClosedDayData(viewModel.selectedDate.dateFormattingOnlyDate())
            }
            commentEdt.setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
            if (context?.isOnline() == true) {
                viewModel.getOrderRejectInfo(args.orderId)
            } else {
                errorMessage(false)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.orderRejectInfo) { orderRejectInfo ->
            viewModel.orderRejectValue = orderRejectInfo
            with(binding) {
                cancelReasonText.text = context?.resources?.getString(
                    R.string.reject_info,
                    orderRejectInfo?.calcPriceFrom,
                    orderRejectInfo?.calcPriceTo,
                    "₽", "${args.statisticData.confirmRateValue}%"
                )
                if (orderRejectInfo?.duplicateOrders.isNullOrEmpty()) {
                    radioGroupContainer.booleanOfRadioButtonView = false
                } else radioGroupContainer.duplicateOrders =
                    orderRejectInfo?.duplicateOrders ?: emptyList()
                radioGroupContainer.setRadioButtons(viewModel.setViewContent())
                shimmer.makeVisibleGone()
                nestedScrollView.makeVisible()
            }
        }
        onEach(viewModel.cancelSuccess) { boolean ->
            if (boolean) {
                context?.getString(R.string.order_canceled)
                    ?.let { Toast(context).showCustomToast(it, this, true) }
                popBackStack()
            }
        }
        onEach(viewModel.cancelError) {
            if (context?.isOnline() == false) {
                context?.getStringRes(R.string.no_internet_toast)
                    ?.let { Toast(context).showCustomToast(it, this@OrderCancelFragment, false) }
            }
        }
        onEach(viewModel.errorGetReject) {
            errorMessage(true)
        }
    }

    override fun onViewClick() {
        with(binding) {
            radioGroupContainer.setOnCalendarClickListener {
                navigateFragment(
                    OrderCancelFragmentDirections.actionOrderCancelFragmentToCalendarFragment(
                        viewModel.orderRejectValue?.busyDays?.toTypedArray()
                            ?: arrayListOf<String>() as Array<out String>,
                        "cancel",
                        viewModel.selectedDate
                    )
                )
            }
            radioGroupContainer.radioGroupClick {
                containerOfCancel.visibility = View.VISIBLE
                activity?.hideSoftKeyboard(commentEdt)
            }
            lConst.setOnClickListener {
                activity?.hideSoftKeyboard(commentEdt)
            }
            commentEdt.setOnClickListener {
                activity?.showSoftKeyboard(commentEdt)
            }
            radioGroupContainer.goToOrderFragment { id, type ->
//                when (type) {
//                    "private" -> navigateFragment(
//                        OrderCancelFragmentDirections.actionOrderCancelFragmentToOrderDetailsIndividualFragment(
//                            id,
//                            0,
//                            type, false,
//                            StatisticsData(
//                                args.statisticData.ordersRateValue,
//                                args.statisticData.bookingRateValue,
//                                args.statisticData.confirmRateValue,
//                                args.statisticData.canOpenContacts
//                            )
//                        )
//                    )
//                    else -> navigateFragment(
//                        OrderCancelFragmentDirections.actionOrderCancelFragmentToUserOrderGroupTourDetailsFragment(
//                            id,
//                            type,
//                            StatisticsData(
//                                args.statisticData.ordersRateValue,
//                                args.statisticData.bookingRateValue,
//                                args.statisticData.confirmRateValue,
//                                args.statisticData.canOpenContacts
//                            )
//                        )
//                    )
//                }
            }
            cancelOrderBtn.setOnClickListener {

                progressBar.makeVisible()
                cancelOrderBtn.text = ""
                commentEdt.hideKeyboard()
                getReasonComment(commentEdt.text.toString())
                radioGroupContainer.itemCheckedId?.let { it1 -> setCancelOrderReason(it1) }
                viewModel.checkedRadioId = radioGroupContainer.itemCheckedId ?: 0

                context?.let {
                    viewModel.cancelButtonClicked(it, args.orderId, args.touristId, args.orderStatus, args.tourType, args.awareStartDt)
                }

                when {
                    !radioGroupContainer.booleanOfRBClick -> {}
                    !radioGroupContainer.booleanOfDT -> {
                        radioGroupContainer.errorDateType = { date ->
                            nestedScrollView.smoothScrollTo(0, date, 800)
                            progressBar.makeVisibleGone()
                            cancelOrderBtn.text = context?.getString(R.string.cancel_order)
                        }
                        radioGroupContainer.setDateColor()
                    }
                    !radioGroupContainer.booleanOfRT -> {
                        radioGroupContainer.errorRadioType = { duplicateOrders ->
                            nestedScrollView.smoothScrollTo(0, duplicateOrders, 700)
                            progressBar.makeVisibleGone()
                            cancelOrderBtn.text = context?.getString(R.string.cancel_order)
                        }
                        context?.getColor(R.color.tomat)
                            ?.let { it2 -> radioGroupContainer.setTitleColor(it2) }
                    }
                    else -> {
                        viewModel.cancelOrderData?.let { it1 ->
                            viewModel.cancelOrder(
                                args.orderId,
                                it1
                            )
                        }
                    }
                }
            }
            issue.update.setOnClickListener {
                isOnlineAgain()
            }
        }
    }

    private fun setCancelOrderReason(checkedId: Int) {
        when (checkedId) {
            14 -> {
                if (binding.radioGroupContainer.booleanOfDT) {
                    viewModel.cancelOrderData =
                        OrderCancelData(
                            checkedId,
                            viewModel.cancelReasonComment,
                            Data(viewModel.selectedDate, null)
                        )
                }
            }
            15 -> {
                if (viewModel.orderRejectValue?.duplicateOrders.isNullOrEmpty()) {
                    viewModel.cancelOrderData =
                        OrderCancelData(checkedId, viewModel.cancelReasonComment, Data(null, null))
                } else {
                    viewModel.orderRejectValue?.duplicateOrders?.forEachIndexed { index, duplicateOrder ->
                        if (index == binding.radioGroupContainer.subItemCheckedId)
                            viewModel.cancelOrderData = OrderCancelData(
                                checkedId,
                                viewModel.cancelReasonComment,
                                Data(null, duplicateOrder.id)
                            )
                    }
                }
            }
            else -> viewModel.cancelOrderData =
                OrderCancelData(checkedId, viewModel.cancelReasonComment, Data(null, null))
        }
    }

    private fun errorMessage(isOnline: Boolean) {
        with(binding) {
            nestedScrollView.makeVisibleGone()
            shimmer.makeVisibleGone()
            issue.containerIssue.makeVisible()
            issue.title.text =
                if (isOnline) context?.getStringRes(R.string.call_error_title) else context?.getString(
                    R.string.no_internet_title
                )
            issue.message.text =
                if (isOnline) context?.getStringRes(R.string.call_error_message) else context?.getString(
                    R.string.no_internet_message
                )
        }
    }

    private fun isOnlineAgain() {
        with(binding) {
            if (context?.isOnline() == true) {
                issue.containerIssue.makeVisibleGone()
                shimmer.makeVisible()
                viewModel.getOrderRejectInfo(args.orderId)
            }
        }
    }

    private fun getReasonComment(comment: String) {
        if (comment != "") {
            viewModel.cancelReasonComment = comment
        }
    }
}