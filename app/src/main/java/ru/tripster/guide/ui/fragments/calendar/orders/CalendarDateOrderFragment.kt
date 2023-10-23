package ru.tripster.guide.ui.fragments.calendar.orders

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.calendar.CalendarDayType
import ru.tripster.domain.model.calendar.GuideDateSchedule
import ru.tripster.domain.model.calendar.SelectedIntervalData
import ru.tripster.domain.model.calendar.filtering.ExperienceResults
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.guide.R
import ru.tripster.guide.Screen
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_BACK_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_CALL_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_CHAT_ICON
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_CLOSE_TIME
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_DELETE
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_EXPERIENCE_FILTER
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_EXPERIENCE_NAME
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.DividerItemDecorator
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentCalendarDateOrderBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainFragment.Companion.NAVIGATE_BUNDLE
import ru.tripster.guide.ui.MainFragment.Companion.NAVIGATE_KEY
import ru.tripster.guide.ui.MainFragment
import ru.tripster.guide.ui.fragments.calendar.CalendarFiltrationBottomSheet
import ru.tripster.guide.ui.fragments.calendar.CalendarFiltrationBottomSheet.Companion.ORDER_ID_REQUEST_KEY
import java.util.*

class CalendarDateOrderFragment :
    FragmentBaseNCMVVM<CalendarDateOrderViewModel, FragmentCalendarDateOrderBinding>() {

    override val viewModel: CalendarDateOrderViewModel by viewModel()
    override val binding: FragmentCalendarDateOrderBinding by viewBinding()
    private val args: CalendarDateOrderFragmentArgs by navArgs()
    private var guideDateScheduleValue: GuideDateSchedule? = null
    private val dateOrdersAdapter =
        DateOrderAdapter(itemClickIndividual = { item ->
            context?.let {
                viewModel.experienceClickedIndividual(
                    it,
                    CALENDAR_DAY_EXPERIENCE_NAME,
                    item.eventData.status,
                    item.eventData.awareStartDt,
                    item.eventData.orders[0].id
                )
            }

            activity?.supportFragmentManager?.setFragmentResult(
                NAVIGATE_KEY, bundleOf(
                    NAVIGATE_BUNDLE to
                            Screen.IndividualOrders.show(
                                item.eventData.orders[0].id,
                                item.eventData.id,
                                item.type,
                                false,
                                Gson().toJson(
                                    StatisticsData(
                                        viewModel.ordersRateValue,
                                        viewModel.bookingRateValue,
                                        viewModel.confirmRateValue,
                                        viewModel.canOpenContacts
                                    )
                                )
                            )
                )
            )

        }, itemClickGroupTour = { item ->
            context?.let {
                viewModel.experienceClickedGroupTour(
                    it,
                    CALENDAR_DAY_EXPERIENCE_NAME,
                    status = item.eventData.status,
                    awareStartDt = item.eventData.awareStartDt,
                    eventNumber = item.id.toInt(),
                    type = item.type
                )
            }

            activity?.supportFragmentManager?.setFragmentResult(
                NAVIGATE_KEY, bundleOf(
                    NAVIGATE_BUNDLE to
                            Screen.GroupTourOrders.show(
                                item.id.toInt(),
                                true,
                                Gson().toJson(
                                    StatisticsData(
                                        viewModel.ordersRateValue,
                                        viewModel.bookingRateValue,
                                        viewModel.confirmRateValue,
                                        viewModel.canOpenContacts
                                    )
                                ), item.eventData.guideLastVisitDate.ifEmpty { currentDate() }
                            )
                )
            )
        }, itemClickConfirmOrder = { id, type, experienceTitle ->
            activity?.supportFragmentManager?.setFragmentResult(
                NAVIGATE_KEY, bundleOf(
                    NAVIGATE_BUNDLE to
                            Screen.ConfirmOrEdit.show(
                                id, type, experienceTitle, true
                            )
                )
            )
        }, itemClickMessage = { orderDetails, awareStartDt ->
            context?.let { context ->
                viewModel.experienceClickedIndividual(
                    context,
                    CALENDAR_DAY_CHAT_ICON,
                    orderDetails.status,
                    awareStartDt,
                    orderDetails.orderId
                )
            }

            val orderDetailsForChat =
                orderDetails.copy(avatar = orderDetails.avatar.replace("/", " "))

            activity?.supportFragmentManager?.setFragmentResult(
                NAVIGATE_KEY, bundleOf(
                    NAVIGATE_BUNDLE to
                            Screen.Chat.show(
                                Gson().toJson(orderDetailsForChat),
                                context?.getString(R.string.date_order_type) ?: "",
                                Gson().toJson(
                                    StatisticsData(
                                        viewModel.ordersRateValue,
                                        viewModel.bookingRateValue,
                                        viewModel.confirmRateValue,
                                        viewModel.canOpenContacts
                                    )
                                )
                            )
                )
            )
        }, {
            val orderDetailsForChat = it.copy(avatar = it.avatar.replace("/", " "))
            activity?.supportFragmentManager?.setFragmentResult(
                NAVIGATE_KEY, bundleOf(
                    NAVIGATE_BUNDLE to
                            Screen.Chat.show(
                                Gson().toJson(orderDetailsForChat),
                                context?.getString(R.string.date_order_type) ?: "",
                                Gson().toJson(
                                    StatisticsData(
                                        viewModel.ordersRateValue,
                                        viewModel.bookingRateValue,
                                        viewModel.confirmRateValue,
                                        viewModel.canOpenContacts
                                    )
                                )
                            )
                )
            )
        }, { phoneNumber, status, awareStartDt, id ->
            context?.let { context ->
                viewModel.experienceClickedIndividual(
                    context,
                    CALENDAR_DAY_CALL_ICON,
                    status,
                    awareStartDt,
                    id
                )
                phoneNumber.phoneCall(context)
            }
        }, { id ->
            context?.let { viewModel.calendarDayItemClicked(it, CALENDAR_DAY_DELETE) }
            viewModel.deleteBusyInterval(id)
            viewModel.isDeleted = true
        })

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        setFragmentResult(
            EXPERIENCE_ID_REQUEST_KEY,
            bundleOf(
                EXPERIENCE_ID_BUNDLE to viewModel.experienceResult,
                CLOSED_TIME_IS_DELETED to viewModel.isDeleted
            )
        )
        removeResultListeners(EXPERIENCE_ID_REQUEST_KEY, ORDER_ID_REQUEST_KEY)
    }

    override fun onView() {
        with(binding) {
            activity?.setLightStatusBar()
            activity?.bottomNavBarVisibility(true)
            toolbar.visibilityOfButtons(true)

            dateOrdersAdapter.date(args.date, viewModel.ordersRateValue)

            if (viewModel.setArgsExperienceTitle) {
                viewModel.argsExperienceId = args.experienceId
                viewModel.setArgsExperienceTitle = false
            }

            if (context?.isOnline() == true) {
                rvShimmer.makeVisible()
                viewModel.getDateOrders(
                    args.date,
                    if (viewModel.experienceId == null) {
                        viewModel.experienceId = viewModel.argsExperienceId
                        viewModel.experienceId
                    } else viewModel.experienceId,

                    viewModel.experienceTitle.ifEmpty { args.experienceTitle },
                )
            } else offlineError()

            toolbar.setFilterButton(viewModel.experienceId != 0)

            activity?.supportFragmentManager?.setFragmentResultListener(
                MainFragment.AMPLITUDE_KEY, viewLifecycleOwner
            ) { _, bundle ->
                val result = bundle.getInt(MainFragment.AMPLITUDE_BUNDLE)
                context?.let {
                    context?.let { context ->
                        viewModel.menuItemClicked(
                            context,
                            result.menuItem()
                        )
                    }
                }
            }

            setFragmentResultListener(ORDER_ID_REQUEST_KEY) { _, bundle ->
                val result =
                    bundle.getParcelable<ExperienceResults>(CalendarFiltrationBottomSheet.ORDER_ID_BUNDLE)

                viewModel.experienceTitle = result?.title ?: ""
                result?.let {
                    getDateOrdersLogic(result.id, result)
                }
                viewModel.resultsList.add(ORDER_ID_REQUEST_KEY)

                if (viewModel.itemsCount == 0) issue.containerIssue.makeVisible()
            }

            viewModel.getStatistics()
            if (isCurrentYear(args.date)) {
                toolbar.setTitle(args.date.dateFormattingOnlyDate())
            } else {
                toolbar.setTitle(args.date.dateWithYear())
            }
            rvOrder.apply {
                context?.let {
                    layoutManager = LinearLayoutManager(it)
                    adapter = dateOrdersAdapter
                    val dividerItemDecoration: DividerItemDecorator? =
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.line_devider
                        )?.let { it1 ->
                            DividerItemDecorator(
                                it1, context.dpToPx(R.dimen.space_16)
                            )
                        }
                    if (dividerItemDecoration != null) {
                        addItemDecoration(dividerItemDecoration)
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewClick() {
        with(binding) {
            toolbar.clickToBackBtn {
                context?.let { context ->
                    viewModel.calendarDayItemClicked(
                        context,
                        CALENDAR_DAY_BACK_ICON
                    )
                }

                activity?.onBackPressed()
            }

            toolbar.clickToFiltration {
                context?.let { context ->
                    viewModel.calendarDayItemClicked(
                        context,
                        CALENDAR_DAY_EXPERIENCE_FILTER
                    )
                }

                navigateFragment(
                    CalendarDateOrderFragmentDirections.actionCalendarDateOrderFragmentToCalendarFiltrationBottomSheet(
                        context?.getString(R.string.calendar_type) ?: ""
                    )
                )
            }

            toolbar.clickToCloseTime {
                context?.let { context ->
                    viewModel.calendarDayItemClicked(
                        context,
                        CALENDAR_DAY_CLOSE_TIME
                    )
                }

                navigateFragment(
                    CalendarDateOrderFragmentDirections.actionCalendarDateOrderFragmentToCalendarCloseTimeFragment(
                        SelectedIntervalData(
                            args.date.convertStringToLocalDate(),
                            null,
                            viewModel.selectedDayDateType
                        )
                    )
                )
            }

            issue.update.setOnClickListener {
                when {
                    context?.isOnline() == true -> {
                        rvShimmer.makeVisible()
                        viewModel.getDateOrders(
                            args.date,
                            viewModel.experienceId,
                            viewModel.experienceTitle
                        )
                        rvOrder.makeVisible()
                        issue.containerIssue.visibility = View.GONE
                    }
                    else -> {
                        offlineError()
                    }
                }
            }
        }
    }


    override fun onEach() {
        onEach(viewModel.statisticsData) { orderStatisticsData ->
            viewModel.ordersRateValue = (orderStatisticsData.ordersRate.value * 100).toInt()
            viewModel.bookingRateValue = (orderStatisticsData.bookingRate.value * 100).toInt()
            viewModel.confirmRateValue =
                (orderStatisticsData.confirmationRate.value * 100).toInt()
            viewModel.canOpenContacts = orderStatisticsData.canOpenTravelerContacts

        }

        onEach(viewModel.dateOrders) { guideDateSchedule ->
            guideDateScheduleValue = guideDateSchedule
            viewModel.experienceId = guideDateSchedule.experienceId
            viewModel.itemsCount = guideDateSchedule.guideScheduleItem.size
            dateOrdersAdapter.submitList(guideDateSchedule.guideScheduleItem)

            guideDateSchedule.guideScheduleItem.forEach {
                if (it.type.contains("busy")) viewModel.isCloseTime = true
                if (it.type.contains("event")) viewModel.isHaveEvent = true
            }

            viewModel.selectedDayDateType = when {
                viewModel.isCloseTime && viewModel.isHaveEvent -> CalendarDayType.BOOKED_ORDER_TIME_CLOSED
                viewModel.isHaveEvent -> CalendarDayType.HAVE_RESERVATION
                else -> CalendarDayType.FREE_DAY
            }

            with(binding) {
                rvShimmer.makeVisibleGone()
                rvOrder.makeVisible()
                usedFilterTextLogic()

                if (guideDateSchedule.guideScheduleItem.isEmpty() && !viewModel.showFilterContainer) {
                    issue.containerIssue.setPadding(0, 0, 0, 0)
                    issue.containerIssue.makeVisible()
                    issue.title.makeVisibleGone()
                    issue.update.makeVisibleGone()
                    issue.message.text = resources.getString(R.string.nothing_planned)
                } else {
                    issue.containerIssue.makeVisibleGone()
                }
            }
        }

        onEach(viewModel.deleteBusyInterval) { deleteSuccess ->
            if (deleteSuccess == true)
                viewModel.getDateOrders(
                    args.date,
                    viewModel.experienceId,
                    viewModel.experienceTitle
                )
        }

        onEach(viewModel.errorDateOrderInfo) {
            if (viewModel.firstTime) {
                callError()
                viewModel.firstTime = false
            } else {
                context?.let {
                    binding.rvShimmer.makeVisibleGone()
                    Toast(it).showCustomToast(it.getString(R.string.no_loading_toast), this, false)
                }
            }
        }
    }

    private fun getDateOrdersLogic(id: Int, result: ExperienceResults) {
        viewModel.experienceId = id
        viewModel.experienceResult = result
        binding.toolbar.setFilterButton(viewModel.experienceId != 0)
        binding.rvShimmer.makeVisible()

        if (context?.isOnline() == true) viewModel.getDateOrders(
            args.date,
            viewModel.experienceId,
            viewModel.experienceTitle
        ) else context?.let {
            binding.rvShimmer.makeVisibleGone()
            Toast(it).showCustomToast(it.getString(R.string.no_internet_toast), this, false)
        }
    }

    private fun callError() {
        with(binding) {
            with(binding) {
                usedFilter.makeVisibleGone()
                rvOrder.makeVisibleGone()
                rvShimmer.makeVisibleGone()
            }
            with(issue) {
                containerIssue.setPadding(0, 0, 0, 0)
                containerIssue.visibility = View.VISIBLE
                title.text = context?.getStringRes(R.string.call_error_title)
                message.text = context?.getStringRes(R.string.call_error_message)
            }
        }
    }

    private fun offlineError() {
        with(binding) {
            usedFilter.makeVisibleGone()
            rvOrder.makeVisibleGone()
            rvShimmer.makeVisibleGone()
            with(issue) {
                containerIssue.setPadding(0, 0, 0, 0)
                containerIssue.visibility = View.VISIBLE
                title.text = context?.getStringRes(R.string.no_internet_title)
                message.text = context?.getStringRes(R.string.no_internet_message)
            }
        }
    }

    private fun usedFilterTextLogic() {
        viewModel.showFilterContainer =
            guideDateScheduleValue?.eventCount != guideDateScheduleValue?.eventTotalCount && viewModel.experienceId != 0

        binding.usedFilter.isVisible = viewModel.showFilterContainer

        val usedFilterText = context?.resources?.getString(
            R.string.used_filter,
            guideDateScheduleValue?.eventCount,
            guideDateScheduleValue?.eventTotalCount
        )
        val coloredText = usedFilterText?.substringAfter(".")
        if (usedFilterText != null && coloredText != null) {
            context?.let {
                binding.usedFilter.setSpannable(
                    usedFilterText,
                    coloredText,
                    ContextCompat.getColor(it, R.color.blue_500)
                )
            }

            binding.usedFilter.setOnClickListener {
                viewModel.showFilterContainer = false
                viewModel.getDateOrders(args.date, 0, "")
                binding.rvShimmer.makeVisible()
                getDateOrdersLogic(0, ExperienceResults.allOrders())
                binding.usedFilter.isVisible = viewModel.showFilterContainer
                binding.issue.containerIssue.makeVisibleGone()
            }

        }
    }

    companion object {
        const val EXPERIENCE_ID_REQUEST_KEY = "EXPERIENCE_ID_REQUEST_KEY"
        const val EXPERIENCE_ID_BUNDLE = "EXPERIENCE_ID_BUNDLE"
        const val CLOSED_TIME_IS_DELETED = "CLOSED_TIME_IS_DELETED"
    }

    private fun isCurrentYear(date: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy", Locale.getDefault())
        val selectedYear = date.substring(0, 4)
        val currentYear = dateFormat.format(Calendar.getInstance().time)
        return selectedYear == currentYear
    }

}