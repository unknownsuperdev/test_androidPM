package ru.tripster.guide.ui.fragments.order

import android.app.AlertDialog
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.DataChip
import ru.tripster.domain.model.events.*
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.guide.R
import ru.tripster.guide.Screen
import ru.tripster.guide.analytics.AnalyticsConstants
import ru.tripster.guide.analytics.AnalyticsConstants.ORDERS_CONFIRM_BUTTON
import ru.tripster.guide.analytics.AnalyticsConstants.ORDERS_TAP_CALL_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.ORDERS_TAP_CHAT_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.ORDERS_TAP_CHAT_PREVIEW
import ru.tripster.guide.analytics.AnalyticsConstants.USER_TAP_EXPERIENCE_NAME
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.adapter.DefaultLoadStateAdapter
import ru.tripster.guide.appbase.adapter.LoadingState
import ru.tripster.guide.appbase.utils.DividerItemDecorator
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentOrderBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment
import ru.tripster.guide.ui.MainFragment.Companion.BADGE_BUNDLE
import ru.tripster.guide.ui.MainFragment.Companion.BADGE_KEY
import ru.tripster.guide.ui.MainFragment.Companion.GET_Y_BUNDLE
import ru.tripster.guide.ui.MainFragment.Companion.PUT_Y_KEY
import ru.tripster.guide.ui.fragments.chat.ChatFragment
import ru.tripster.guide.ui.fragments.confirmOrder.ConfirmOrEditOrderFragment
import ru.tripster.guide.ui.fragments.orderDetails.individual.OrderDetailsIndividualFragment
import java.util.*
import kotlin.math.abs

class OrdersFragment : FragmentBaseNCMVVM<OrderViewModel, FragmentOrderBinding>() {
    override val viewModel: OrderViewModel by viewModel()
    override val binding: FragmentOrderBinding by viewBinding()
    private val args: OrdersFragmentArgs by navArgs()
    private var isSubmitted = false
    private var ordersRateValue: Int = 0
    private var bookingRateValue: Int = 0
    private var confirmRateValue: Int = 0
    private var canOpenContacts: Boolean = false
    private val ordersAdapter =
        OrdersAdapter(itemClickIndividual = { item ->
            context?.let {
                viewModel.experienceClickedIndividual(
                    it,
                    USER_TAP_EXPERIENCE_NAME,
                    item.status,
                    item.awareStartDt,
                    item.initiator,
                    item.orderId
                )
            }

            navigateFragment(
                OrdersFragmentDirections.actionOrderFragmentToOrderDetailsIndividualFragment(
                    item.orderId,
                    item.eventId,
                    item.type,
                    false,
                    Gson().toJson(
                        StatisticsData(
                            ordersRateValue,
                            bookingRateValue,
                            confirmRateValue,
                            canOpenContacts
                        )
                    )
                )
            )

        }, itemClickGroupTour = { eventResult ->

            context?.let {
                viewModel.experienceClickedGroupTour(
                    it,
                    USER_TAP_EXPERIENCE_NAME,
                    status = eventResult.status,
                    awareStartDt = eventResult.awareStartDt,
                    eventNumber = eventResult.id,
                    type = eventResult.experience.type
                )
            }

            navigateFragment(
                OrdersFragmentDirections.actionOrderFragmentToOrderDetailsGroupFragment(
                    eventResult.id, false,
                    Gson().toJson(
                        StatisticsData(
                            ordersRateValue,
                            bookingRateValue,
                            confirmRateValue,
                            canOpenContacts
                        )
                    ),
                    eventResult.guideLastVisitDate
                )
            )
        }, itemClickConfirmOrder = { id, type, experienceTitle, status, awareStartDt, initiator ->
            context?.let {
                viewModel.experienceClickedIndividual(
                    it,
                    ORDERS_CONFIRM_BUTTON,
                    status,
                    awareStartDt,
                    initiator,
                    id
                )
            }

            navigateFragment(
                OrdersFragmentDirections.actionOrderFragmentToConfirmOrderFragment(
                    id,
                    type,
                    experienceTitle,
                    true
                )
            )
        }, itemClickMessage = { orderDetailsForChat, awareStartDt, initiator ->
            context?.let {
                viewModel.experienceClickedIndividual(
                    it,
                    ORDERS_TAP_CHAT_ICON,
                    orderDetailsForChat.status,
                    awareStartDt,
                    initiator,
                    orderDetailsForChat.orderId
                )
            }

            navigateFragment(
                OrdersFragmentDirections.actionOrderFragmentToChatFragment(
                    Gson().toJson(orderDetailsForChat), "",
                    Gson().toJson(
                        StatisticsData(
                            ordersRateValue,
                            bookingRateValue,
                            confirmRateValue,
                            canOpenContacts
                        )
                    )
                )
            )
        }, itemClickComment = { orderDetailsForChat, awareStartDt, initiator ->
            context?.let {
                viewModel.experienceClickedIndividual(
                    it,
                    ORDERS_TAP_CHAT_PREVIEW,
                    orderDetailsForChat.status,
                    awareStartDt,
                    initiator,
                    orderDetailsForChat.orderId
                )
            }

            navigateFragment(
                OrdersFragmentDirections.actionOrderFragmentToChatFragment(
                    Gson().toJson(orderDetailsForChat), context?.getString(R.string.orders) ?: "",
                    Gson().toJson(
                        StatisticsData(
                            ordersRateValue,
                            bookingRateValue,
                            confirmRateValue,
                            canOpenContacts
                        )
                    )
                )
            )
        }) { phoneNumber, status, id, awareStartDt, initiator ->
            context?.let {
                viewModel.experienceClickedIndividual(
                    it,
                    ORDERS_TAP_CALL_ICON,
                    status,
                    awareStartDt,
                    initiator,
                    id
                )
            }

            if (status != Status.PAID.value) {
                if (ordersRateValue >= 60) {
                    navigateFragment(
                        OrdersFragmentDirections.actionOrderFragmentToRulesOfViewContactBottomSheet(
                            id,
                            context?.getString(R.string.orders) ?: "",
                            phoneNumber
                        )
                    )
                } else {
                    navigateFragment(
                        OrdersFragmentDirections.actionOrderFragmentToSeeContactWarningBottomSheet(
                            StatisticsData(
                                ordersRateValue,
                                bookingRateValue,
                                confirmRateValue,
                                canOpenContacts
                            ),
                            context?.getString(R.string.orders) ?: ""
                        )
                    )
                }
            } else context?.let { phoneNumber.phoneCall(it) }

        }

    override fun onView() {
        activity?.setLightStatusBar()
        activity?.bottomNavBarVisibility(true)

        viewModel.delayLayer = false

        (activity as MainActivity).supportFragmentManager.setFragmentResult(
            BADGE_KEY,
            bundleOf(BADGE_BUNDLE to 0)
        )

        activity?.supportFragmentManager?.setFragmentResultListener(
            MainFragment.AMPLITUDE_KEY, viewLifecycleOwner
        ) { _, bundle ->
            val result = bundle.getInt(MainFragment.AMPLITUDE_BUNDLE)
            context?.let {
                context?.let { context -> viewModel.menuItemClicked(context, result.menuItem()) }
            }
        }

        activity?.supportFragmentManager?.setFragmentResultListener(
            MainFragment.GET_Y_KEY,
            viewLifecycleOwner
        ) { _, result ->
            viewModel.isScrollable =
                result.getInt(GET_Y_BUNDLE) - binding.container.bottom < binding.rvOrder.computeVerticalScrollRange()
        }

        setFragmentResultListener(
            ConfirmOrEditOrderFragment.CONFIRM_ORDER_REQUEST_KEY
        ) { _, bundle ->
            val result =
                bundle.getParcelable<OrderDetails>(ConfirmOrEditOrderFragment.CONFIRM_ORDER_BUNDLE)
            if (result?.isConfirm == true) {
                context?.getString(R.string.order_confirmed)
                    ?.let { Toast(context).showCustomToast(it, this, true) }
            } else {
                context?.getString(R.string.order_edited)
                    ?.let { Toast(context).showCustomToast(it, this, true) }
            }
        }

        with(binding) {
            rvShimmer.startShimmer()
            chipShimmer.startShimmer()

            currentDate()

            context?.let { customAppBar.setTitle(it.getStringRes(R.string.order)) }
            rvOrder.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it)
                    adapter = ordersAdapter.withLoadStateHeaderAndFooter(
                        header = DefaultLoadStateAdapter(),
                        footer = DefaultLoadStateAdapter()
                    )

                    val dividerItemDecoration: DividerItemDecorator? = ContextCompat.getDrawable(
                        context,
                        R.drawable.line_devider
                    )?.let { it1 ->
                        DividerItemDecorator(
                            it1,
                            context.dpToPx(R.dimen.space_16)
                        )
                    }
                    if (dividerItemDecoration != null) {
                        addItemDecoration(dividerItemDecoration)
                    }
                }

                binding.customAppBar.setExpanded(abs(viewModel.currentProgress) <= viewModel.totalScrollRange / 2)

                customAppBar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->

                    if (abs(verticalOffset) - appBarLayout.totalScrollRange == 0 && viewModel.isScrollable) {
                        line.makeVisible()
                    } else {
                        line.makeVisibleGone()
                    }

                    if (ordersAdapter.itemCount == 0) line.makeVisibleGone()

                    viewModel.currentProgress = verticalOffset
                    viewModel.totalScrollRange = appBarLayout.totalScrollRange
                })

                issue.update.setOnClickListener {
                    when {
                        context?.isOnline() == true -> {
                            viewModel.getEventCounters()
                            issue.containerIssue.makeVisibleGone()
                            chipShimmer.makeVisible()
                            layer.makeVisible()
                        }
                        else -> {
                            offlineError()
                        }
                    }
                }
            }

            if (context?.isOnline() == true) {
                if (args.deepLinkOrderId == 0) {
//                    if (viewModel.loadingState == LoadingState.LOADING)
                        viewModel.getEventCounters()
                } else {
                    if (!viewModel.backPressed)
                        viewModel.getOrderDetails(
                            args.deepLinkOrderId
                        )
                    else viewModel.getEventCounters()
                }
            } else {
                offlineError()
            }

            ordersAdapter.observeLoadState(viewLifecycleOwner) { state, _ ->
                when (state) {
                    LoadingState.LOADED -> {
                        scrollContentLogic()

                        if (viewModel.isLoaded) with(binding) {
                            if (ordersAdapter.itemCount == 0 && !viewModel.isReturned) {
                                viewVisibilities(context?.getViewName(R.id.emptyList))
                                if (viewModel.totalCount == 0) {
                                    emptyList.title.makeVisible()
                                    container.makeVisibleGone()
                                    emptyList.text.text = context?.getString(R.string.no_order_text)
                                } else {
                                    emptyList.title.makeVisibleGone()
                                    emptyList.text.text = emptyListCase(viewModel.chipId.value)
                                }
                            } else {
                                viewVisibilities(context?.getViewName(R.id.rvShimmer))
                            }
                        }
                        viewModel.isReturned = false
//                        viewModel.loadingState = LoadingState.LOADED
                    }

                    LoadingState.LOADING -> {
//                        viewModel.loadingState = LoadingState.LOADING
                    }

                    LoadingState.ERROR -> {
//                        viewModel.loadingState = LoadingState.ERROR

                        if (context?.isOnline() == false) {
                            if (binding.rvOrder.adapter?.itemCount == 0 && !isSubmitted) {
                                offlineError()
                            } else {
                                context?.getStringRes(R.string.no_internet_toast)
                                    ?.let {
                                        Toast(context).showCustomToast(
                                            it,
                                            this@OrdersFragment,
                                            false
                                        )
                                    }
                            }
                        } else {
                            if (binding.rvOrder.adapter?.itemCount == 0 && !isSubmitted) {
                                callError()
                            } else {
                                context?.getStringRes(R.string.no_loading_toast)
                                    ?.let {
                                        Toast(context).showCustomToast(
                                            it,
                                            this@OrdersFragment,
                                            false
                                        )
                                    }
                            }
                        }
                    }
                }
            }

            setFragmentResultListener(OrderDetailsIndividualFragment.IS_MODIFIED_KEY) { _, bundle ->

                viewModel.isModified =
                    bundle.getBoolean(OrderDetailsIndividualFragment.IS_MODIFIED_BUNDLE)
            }

            setFragmentResultListener(ConfirmOrEditOrderFragment.IS_CONFIRMED_KEY) { _, bundle ->
                viewModel.isModified =
                    bundle.getBoolean(ConfirmOrEditOrderFragment.IS_CONFIRMED_BUNDLE)
            }
            setFragmentResultListener(ChatFragment.VIEWED_BY_GUIDE_KEY) { _, bundle ->
                viewModel.isModified =
                    bundle.getBoolean(ChatFragment.VIEWED_BY_GUIDE_BUNDLE)
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.statisticsData) { orderStatisticsData ->
            ordersRateValue = (orderStatisticsData.ordersRate.value * 100).toInt()
            bookingRateValue = (orderStatisticsData.bookingRate.value * 100).toInt()
            confirmRateValue = (orderStatisticsData.confirmationRate.value * 100).toInt()
            canOpenContacts = orderStatisticsData.canOpenTravelerContacts
        }

        onEach(viewModel.pagingData) {
            if (viewModel.secondEnter) {
                isSubmitted = true
                ordersAdapter.submitData(lifecycle, it)
            }
            viewModel.secondEnter = true
        }

        onEach(viewModel.orderDetails) {

            if (viewModel.backPressed) {
                viewModel.isReturned = true
                return@onEach
            }

            viewModel.backPressed = true

            val type =
                if (it?.event?.isForGroups == true) context?.getStringRes(R.string.group_type) else context?.getStringRes(
                    R.string.private_type
                )

            if (it?.event?.isForGroups == false)
                navigateFragment(
                    OrdersFragmentDirections.actionOrderFragmentToOrderDetailsIndividualFragment(
                        it.id, it.id, type ?: "", false,
                        Gson().toJson(
                            StatisticsData(
                                ordersRateValue,
                                bookingRateValue,
                                confirmRateValue,
                                canOpenContacts
                            )
                        )
                    )
                )
            else navigateFragment(
                OrdersFragmentDirections.actionOrderFragmentToUserOrderGroupTourDetailsFragment(
                    it?.id ?: 0, type ?: "",
                    StatisticsData(
                        ordersRateValue,
                        bookingRateValue,
                        confirmRateValue,
                        canOpenContacts
                    )
                )
            )
        }

        onEach(viewModel.eventCounters) { eventCounters ->
            viewModel.isLoaded = true
            if (activity is MainActivity) {
                eventCounters?.let {
                    (activity as MainActivity).supportFragmentManager.setFragmentResult(
                        BADGE_KEY,
                        bundleOf(BADGE_BUNDLE to it.needAttention)
                    )
                }
            }

            with(binding) {
                container.makeVisible()
                val chipList = listOf(
                    eventCounters?.inWork?.let {
                        context?.getStringRes(R.string.all_at_work)?.let { it1 ->
                            DataChip(
                                0, it1,
                                it
                            )
                        }
                    },
                    eventCounters?.unread?.let {
                        context?.getStringRes(R.string.unread)?.let { it1 ->
                            DataChip(
                                1, it1,
                                it
                            )
                        }
                    },
                    eventCounters?.confirmation?.let {
                        context?.getStringRes(R.string.waiting_for_confirmation)?.let { it1 ->
                            DataChip(
                                2, it1,
                                it
                            )
                        }
                    },
                    eventCounters?.pendingPayment?.let {
                        context?.getStringRes(R.string.pending_payment)?.let { it1 ->
                            DataChip(
                                3, it1,
                                it
                            )
                        }
                    },
                    eventCounters?.booked?.let {
                        context?.getStringRes(R.string.booked)?.let { it1 ->
                            DataChip(
                                4, it1,
                                it
                            )
                        }
                    },
                    eventCounters?.finished?.let {
                        context?.getStringRes(R.string.completed)?.let { it1 ->
                            DataChip(
                                5, it1, null
                            )
                        }
                    },
                    eventCounters?.canceled?.let {
                        context?.getStringRes(R.string.canceled)?.let { it1 ->
                            DataChip(
                                6, it1,
                                null
                            )
                        }
                    }
                )
                container.removeAllViews()
                setupChip(chipList)
                chipShimmer.stopShimmer()
                chipShimmer.visibility = View.GONE
                container.visibility = View.VISIBLE
            }
        }

        onEach(viewModel.errorOrderDetails) {
            if (viewModel.backPressed) return@onEach else {
                showDialog(
                    resources.getString(R.string.loginDialogTitle),
                    resources.getString(R.string.this_order_is_not_found),
                    resources.getString(R.string.ok)
                )
            }
        }

        onEach(viewModel.profileInfo) {
            with(viewModel) {
                guidEmail = it.email
                guidId = it.id
            }
        }
    }

    private fun showDialog(
        titleText: String,
        bodyText: String,
        positiveBtnTxt: String
    ) {
        val builder = AlertDialog.Builder(context, R.style.Background_transparent).create()
        val view = layoutInflater.inflate(R.layout.dialog_login, null)
        val body = view.findViewById(R.id.bodyTv) as TextView
        val title = view.findViewById(R.id.titleTv) as TextView
        val okBtn = view.findViewById(R.id.okBtn) as TextView
        val cancelBtn = view.findViewById(R.id.cancelBtn) as TextView
        with(builder) {
            setView(view)
            cancelBtn.makeVisibleGone()
            body.text = bodyText
            title.text = titleText
            okBtn.text = positiveBtnTxt
            okBtn.setOnClickListener {
                viewModel.getEventCounters()
                viewModel.backPressed = true
                dismiss()
            }
            setCanceledOnTouchOutside(false)
            show()
        }
    }

    private fun setupChip(list: List<DataChip?>): ChipGroup {
        val chipGroup = ChipGroup(requireContext())
        if (viewModel.currentChipData.isEmpty()) {
            list.forEach { currentChipData ->
                if (currentChipData?.count != null)
                    viewModel.currentChipData += currentChipData.count
            }
        } else {
            viewModel.chipData = ""
            list.forEach { chipData ->
                if (chipData?.count != null)
                    viewModel.chipData += chipData.count
            }
            viewModel.eventBack = viewModel.currentChipData == viewModel.chipData
            viewModel.currentChipData = viewModel.chipData
        }
        filterOrderList(viewModel.chipId.value)

        if (binding.container.childCount == 0) {
            binding.container.addView(chipGroup)
            with(chipGroup) {
                isSingleSelection = true
                isSelectionRequired = true
                isSingleLine = true
                setChipSpacing(resources.getDimension(R.dimen.space_8).toInt())
                setPadding(
                    resources.getDimension(R.dimen.space_16).toInt(),
                    resources.getDimension(R.dimen.space_12).toInt(),
                    resources.getDimension(R.dimen.space_16).toInt(),
                    resources.getDimension(R.dimen.space_12).toInt()
                )
            }

            list.forEach { dataChip ->
                val chip = Chip(requireContext(), null, R.style.ChipStyle)
                chip.id = dataChip?.id ?: 0
                with(chip) {
                    setTextColor(
                        ContextCompat.getColorStateList(
                            requireContext(),
                            R.color.selector_white_gray
                        )
                    )
                    chipBackgroundColor =
                        ContextCompat.getColorStateList(requireContext(), R.color.selector_chip)
                    text = if (dataChip?.count != null) {
                        initChipText(dataChip.name, dataChip.count)
                    } else dataChip?.name
                    isCheckable = true
                    checkedIcon = null
                    isClickable = true

                    setTextAppearance(R.style.ChipTextStyle)
                    chip.setChipMinHeightResource(R.dimen.space_36)
                    chip.setEnsureMinTouchTargetSize(false)

                    chip.setOnClickListener {
                        viewModel.isReadyToScrollCount = 0
                        if (chip.id != viewModel.chipId.value) {
                            viewModel.delayLayer = true
                            viewModel.getEventCounters()
                            viewModel.eventBack = false
                            filterOrderList(dataChip?.id)
                        } else {
                            viewModel.delayLayer = false
                            viewModel.getEventCounters()
                            binding.rvOrder.scrollToPosition(0)
                        }
                    }
                }
                chipGroup.addView(chip)
                chipGroup.check(viewModel.chipId.value)
            }
        }
        return chipGroup
    }

    private fun filterOrderList(id: Int?) {
        initShimmer()

        if (context?.isOnline() == true) {
            when (id) {
                0 -> {
                    context?.getStringRes(R.string.sorting_unread_first)?.let {
                        val queryType = EventsCallSortingModel(
                            isInWork = true,
                            status = StatusStates.NONE,
                            sorting = it
                        )
                        if (!viewModel.eventBack || viewModel.isModified) {
                            viewModel.changeQuery(queryType)
                            viewModel.eventBack = true
                            viewModel.isModified = false
                        }

                    }

                    context?.let {
                        viewModel.chipName = it.getStringRes(R.string.all_at_work)
                    }
                }

                1 -> {
                    context?.getStringRes(R.string.sorting_last_modified_at_reverse)?.let {
                        val queryType = EventsCallSortingModel(
                            StatusStates.NONE,
                            unreadComments = 0,
                            sorting = it
                        )
                        if (!viewModel.eventBack || viewModel.isModified) {
                            viewModel.changeQuery(queryType)
                            viewModel.eventBack = true
                            viewModel.isModified = false
                        }
                    }

                    context?.let {
                        viewModel.chipName = it.getStringRes(R.string.unread)
                    }
                }

                2 -> {
                    context?.getStringRes(R.string.sorting_unread_first)?.let {
                        val queryType =
                            EventsCallSortingModel(StatusStates.CONFIRM, sorting = it)
                        if (!viewModel.eventBack || viewModel.isModified) {
                            viewModel.changeQuery(queryType)
                            viewModel.eventBack = true
                            viewModel.isModified = false
                        }
                    }

                    context?.let {
                        viewModel.chipName = it.getStringRes(R.string.waiting_for_confirmation)
                    }
                }

                3 -> {
                    context?.getStringRes(R.string.sorting_unread_first)?.let {
                        val queryType =
                            EventsCallSortingModel(StatusStates.PENDING_PAYMENT, sorting = it)
                        if (!viewModel.eventBack || viewModel.isModified) {
                            viewModel.changeQuery(queryType)
                            viewModel.eventBack = true
                            viewModel.isModified = false
                        }
                    }

                    context?.let {
                        viewModel.chipName = it.getStringRes(R.string.pending_payment)
                    }
                }

                4 -> {
                    context?.getStringRes(R.string.sorting_starts_at)?.let {
                        val queryType =
                            EventsCallSortingModel(
                                StatusStates.BOOKED,
                                sorting = it,
                                startDate = getCurrentDateInIso8601Format()
                            )
                        if (!viewModel.eventBack || viewModel.isModified) {
                            viewModel.changeQuery(queryType)
                            viewModel.eventBack = true
                            viewModel.isModified = false
                        }
                    }

                    context?.let {
                        viewModel.chipName = it.getStringRes(R.string.booked)
                    }
                }

                5 -> {
                    context?.getStringRes(R.string.sorting_starts_at_reverse)?.let {
                        val queryType =
                            EventsCallSortingModel(
                                StatusStates.BOOKED,
                                sorting = it,
                                startDate = getCurrentDateInIso8601Format()
                            )
                        if (!viewModel.eventBack || viewModel.isModified) {
                            viewModel.changeQuery(queryType)
                            viewModel.eventBack = true
                            viewModel.isModified = false
                        }
                    }

                    context?.let {
                        viewModel.chipName = it.getStringRes(R.string.completed)
                    }
                }

                6 -> {
                    context?.getStringRes(R.string.sorting_last_modified_at_reverse)?.let {
                        val queryType =
                            EventsCallSortingModel(StatusStates.CANCELLED, sorting = it)
                        if (!viewModel.eventBack || viewModel.isModified) {
                            viewModel.changeQuery(queryType)
                            viewModel.eventBack = true
                            viewModel.isModified = false
                        }
                    }

                    context?.let {
                        viewModel.chipName = it.getStringRes(R.string.canceled)
                    }
                }
            }
        } else offlineError()

        viewModel.chipId.value = id ?: 0

        context?.let {
            viewModel.chipClick(it)
        }
    }

    private fun initChipText(string: String, count: Int?): Spannable {
        val fullText = "$string $count"
        val textToSpan: Spannable = SpannableString(fullText)
        val countPosition = fullText.indexOf(count.toString())
        textToSpan.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(requireContext(), R.color.gray)),
            countPosition,
            countPosition + count.toString().length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )

        return textToSpan
    }

    private fun viewVisibilities(view: String?) {
        when (view) {
            context?.getViewName(R.id.rvShimmer) -> {
                with(binding) {
                    issue.containerIssue.visibility = View.GONE
                    emptyList.containerEmptyList.visibility = View.GONE
                    rvOrder.visibility = View.VISIBLE

                    lifecycleScope.launch {
                        if (viewModel.delayLayer) delay(900)
                        layer.visibility = View.GONE
                    }
                }
            }
            context?.getViewName(R.id.issue) -> {
                with(binding) {
                    issue.containerIssue.visibility = View.VISIBLE
                    layer.visibility = View.GONE
                    rvOrder.visibility = View.GONE
                    emptyList.containerEmptyList.visibility = View.GONE
                    container.visibility = View.GONE
                    chipShimmer.visibility = View.GONE
                }
            }
            context?.getViewName(R.id.emptyList) -> {
                with(binding) {
                    emptyList.containerEmptyList.visibility = View.VISIBLE
                    issue.containerIssue.visibility = View.GONE
                    layer.visibility = View.GONE
                    rvOrder.visibility = View.GONE
                }
            }
        }
    }

    private fun emptyListCase(id: Int): String? {
        return when (id) {
            0 -> context?.getStringRes(R.string.no_orders_in_work)
            1 -> context?.getStringRes(R.string.no_unread)
            2 -> context?.getStringRes(R.string.no_waiting_for_confirmation)
            3 -> context?.getStringRes(R.string.no_pending_payment)
            4 -> context?.getStringRes(R.string.no_booked)
            5 -> context?.getStringRes(R.string.no_completed)
            else -> context?.getStringRes(R.string.no_canceled)
        }
    }

    private fun initShimmer() {
        with(binding) {
            rvOrder.visibility = View.GONE
            emptyList.containerEmptyList.visibility = View.GONE
            layer.visibility = View.VISIBLE
            issue.containerIssue.visibility = View.GONE
            rvShimmer.startShimmer()
        }
    }

    private fun offlineError() {
        with(binding) {
            chipShimmer.visibility = View.GONE
            container.makeVisibleGone()
            layer.visibility = View.GONE
            with(issue) {
                containerIssue.visibility = View.VISIBLE
                title.text = context?.getStringRes(R.string.no_internet_title)
                message.text = context?.getStringRes(R.string.no_internet_message)
            }
        }
    }

    private fun scrollContentLogic() {
        binding.rvOrder.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (isAdded) {
                    with(binding) {

                        activity?.supportFragmentManager?.setFragmentResult(PUT_Y_KEY, bundleOf())

                        if (ordersAdapter.itemCount == 0) customAppBar.setScrollFlag(false) else
                            customAppBar.setScrollFlag(viewModel.isScrollable)

                        if (!viewModel.isScrollable) line.makeVisibleGone()

                    }
                    binding.rvOrder.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            }
        })
    }

    private fun callError() {
        with(binding) {
            viewVisibilities(context?.getViewName(R.id.issue))
            with(issue) {
                title.text = context?.getStringRes(R.string.call_error_title)
                message.text = context?.getStringRes(R.string.call_error_message)
            }
        }
    }

    override fun onStop() {
        super.onStop()

//        ordersAdapter.removeLoadStateListener()
    }

    private fun currentDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(Calendar.getInstance().time)

}