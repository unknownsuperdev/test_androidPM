package ru.tripster.guide.ui.fragments.calendar.chooseDate

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.calendar.*
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.guide.Constants
import ru.tripster.guide.R
import ru.tripster.guide.Screen
import ru.tripster.guide.appbase.CalendarScrollListener
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.CalendarDayBinding
import ru.tripster.guide.databinding.CalendarHeaderBinding
import ru.tripster.guide.databinding.FragmentCalendarChooseTimeBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment.Companion.NAVIGATE_BUNDLE
import ru.tripster.guide.ui.MainFragment.Companion.NAVIGATE_KEY
import ru.tripster.guide.ui.calendar.model.CalendarDay
import ru.tripster.guide.ui.calendar.model.CalendarMonth
import ru.tripster.guide.ui.calendar.model.DayOwner
import ru.tripster.guide.ui.calendar.model.WeekDays
import ru.tripster.guide.ui.calendar.ui.DayBinder
import ru.tripster.guide.ui.calendar.ui.MonthHeaderFooterBinder
import ru.tripster.guide.ui.calendar.ui.ViewContainer
import ru.tripster.guide.ui.calendar.utils.daysOfWeekFromLocale
import ru.tripster.guide.ui.fragments.calendar.CalendarFiltrationBottomSheetArgs
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class CalendarChooseDateFragment :
    FragmentBaseNCMVVM<CalendarChooseDateViewModel, FragmentCalendarChooseTimeBinding>() {

    override val viewModel: CalendarChooseDateViewModel by viewModel()
    override val binding: FragmentCalendarChooseTimeBinding by viewBinding()

    private val args: CalendarChooseDateFragmentArgs by navArgs()
    private val today = LocalDate.now()
    private var selectAnotherStartDate: (() -> Unit)? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            popBackStack()
        }
    }

    override fun onView() {
        activity?.bottomNavBarVisibility(true)
        activity?.setLightStatusBar()

        viewModel.getStatistics()

        viewModel.lastDate =
            if (args.startDate.isNotEmpty()) args.startDate.calcMonthForFullMonth(3) else viewModel.firstDate.calcMonthForFullMonth(
                3
            )

        getScheduleDataForAllLoadedDates()
//        viewModel.getGuidesSchedule(viewModel.firstDate, viewModel.lastDate, null, null)

        initMonths(viewModel.firstDate, viewModel.lastDate)

        if (args.startDate.isNotEmpty()) binding.calendarView.scrollToMonth(
            YearMonth.parse(
                args.startDate.dropLast(
                    3
                )
            )
        )

        chooseDateBtnVisibilities()

        with(binding) {
            calendarView.apply {

                addOnScrollListener(object :
                    CalendarScrollListener(layoutManager as LinearLayoutManager, true) {

                    override fun loadMoreItems(firstVisibleItemPosition: Int) {
                        viewModel.isLoading = true

                        loadMoreShimmer.makeVisible()

                        viewModel.getGuidesSchedule(
                            viewModel.lastDate,
                            viewModel.lastDate.calcMonthForFullMonth(3),
                            null,
                            null
                        )

                        viewModel.lastCallStartDate =
                            viewModel.lastDate

                        viewModel.lastCallEndDate = viewModel.lastDate.calcMonthForFullMonth(3)

                        viewModel.lastDate = viewModel.lastCallEndDate
                    }

                    override val isLoading: Boolean
                        get() = viewModel.isLoading
                })
            }
        }

        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val bindingDay = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {
                    selectedDaysInterval(day.date)
                }
            }
        }

        binding.calendarView.dayBinder =
            object : DayBinder<DayViewContainer> {
                override fun create(view: View) = DayViewContainer(view)
                override fun bind(
                    container: DayViewContainer,
                    day: CalendarDay
                ) {
                    container.day = day
                    val textView = container.bindingDay.day
                    val circle = container.bindingDay.circle
                    val circleSecond = container.bindingDay.circleSecond
                    val containerDay = container.bindingDay.container
                    textView.text = day.date.dayOfMonth.toString()
                    containerDay.isClickable = false

                    selectAnotherStartDate = {
                        containerDay.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.shape_circle_white
                        )
                        textView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_20
                            )
                        )
                    }

                    onEach(viewModel.guidesSchedule) {
                        if (day.owner == DayOwner.PREVIOUS_MONTH) textView.makeVisibleGone() else textView.makeVisible()
                        if (day.owner == DayOwner.NEXT_MONTH) textView.makeVisibleGone() else textView.makeVisible()

                        if (day.owner == DayOwner.THIS_MONTH) {
                            when (day.date) {
                                viewModel.firstSelectedDate, viewModel.secondSelectedDate -> {
                                    viewModel.scheduleList.forEach {
                                        if (it.date == day.date.toString()) {
                                            containerDay.isClickable = true
                                            when (it.dayType) {
                                                CalendarDayType.CLOSED_DAY -> {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white_transparent_60
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white_transparent_60)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()
                                                }
                                                CalendarDayType.HAVE_RESERVATION -> {
                                                    viewModel.selectedDayDateType = it.dayType
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white)
                                                        .into(circle)
                                                    circle.makeVisible()
                                                    circleSecond.makeVisibleGone()
                                                }
                                                CalendarDayType.FREE_DAY -> {
                                                    viewModel.selectedDayDateType = it.dayType
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    circleSecond.makeVisibleGone()
                                                }
                                                CalendarDayType.CLOSED_TIME -> {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white_transparent_60)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()
                                                }
                                                CalendarDayType.BOOKED_ORDER_TIME_CLOSED -> {
                                                    viewModel.selectedDayDateType = it.dayType
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white)
                                                        .into(circle)
                                                    circle.makeVisible()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white_transparent_60)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()
                                                }
                                            }

                                        }
                                    }

                                    if (viewModel.secondSelectedDate != null) {
                                        if (day.date == viewModel.firstSelectedDate && day.date.dayOfWeek.name != WeekDays.SUNDAY.name && !day.date.toString()
                                                .isLastDayOfMonth()
                                        ) {
                                            containerDay.background = ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.calendar_item_iterval_first
                                            )
                                        } else if (day.date == viewModel.secondSelectedDate && day.date.dayOfWeek.name != WeekDays.MONDAY.name && day.date.dayOfMonth != 1) {
                                            containerDay.background = ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.calendar_item_iterval_second
                                            )
                                        } else containerDay.background = ContextCompat.getDrawable(
                                            requireContext(),
                                            R.drawable.calendar_item_selected
                                        )

                                    } else containerDay.background = ContextCompat.getDrawable(
                                        requireContext(),
                                        R.drawable.calendar_item_selected
                                    )
                                }

                                else -> {
                                    if (viewModel.secondSelectedDate != null && day.date >= viewModel.firstSelectedDate && day.date <= viewModel.secondSelectedDate) {
                                        if ((day.date.dayOfWeek.name == WeekDays.MONDAY.name && day.date.toString()
                                                .isLastDayOfMonth()) || (day.date.dayOfWeek.name == WeekDays.SUNDAY.name && day.date.dayOfMonth == 1)
                                        ) {
                                            containerDay.background = ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.calendar_item_interval_corners
                                            )
                                        } else if (day.date.dayOfWeek.name == WeekDays.SUNDAY.name || day.date.toString()
                                                .isLastDayOfMonth()
                                        ) {
                                            containerDay.background = ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.calendar_item_interval_right_corners
                                            )
                                        } else if (day.date.dayOfWeek.name == WeekDays.MONDAY.name || day.date.dayOfMonth == 1) {
                                            containerDay.background = ContextCompat.getDrawable(
                                                requireContext(),
                                                R.drawable.calendar_item_interval_left_corners
                                            )
                                        } else {
                                            containerDay.background =
                                                ContextCompat.getDrawable(
                                                    requireContext(),
                                                    R.drawable.calendar_item_interval_rectangle
                                                )
                                        }

                                    } else containerDay.background = null

                                    viewModel.scheduleList.forEach {
                                        if (it.date == day.date.toString()) {
                                            containerDay.isClickable = true
                                            when (it.dayType) {
                                                CalendarDayType.CLOSED_DAY -> {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white_transparent_60
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white_transparent_60)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.gray_70
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_grey_70)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()

                                                }
                                                CalendarDayType.HAVE_RESERVATION -> {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white)
                                                        .into(circle)
                                                    circle.makeVisible()
                                                    circleSecond.makeVisibleGone()
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.gray_20
                                                        )
                                                    )
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_green)
                                                        .into(circle)
                                                    circle.makeVisible()
                                                    circleSecond.makeVisibleGone()

                                                }
                                                CalendarDayType.FREE_DAY -> {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    circleSecond.makeVisibleGone()
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.gray_20
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    circleSecond.makeVisibleGone()
                                                }
                                                CalendarDayType.CLOSED_TIME -> {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_white_transparent_60)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.gray_20
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_grey_70)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()
                                                }
                                                CalendarDayType.BOOKED_ORDER_TIME_CLOSED -> {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.gray_20
                                                        )
                                                    )
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_green)
                                                        .into(circle)
                                                    circle.makeVisible()
                                                    Glide.with(requireContext())
                                                        .load(R.drawable.shape_circle_grey_70)
                                                        .into(circleSecond)
                                                    circleSecond.makeVisible()

                                                }
                                            }
                                        }
                                    }

                                    if (day.date == today) {
                                        textView.typeface =
                                            resources.getFont(R.font.roboto_bold_700)
                                    }
                                }
                            }
                        } else {
                            containerDay.background = null
                            textView.makeVisibleGone()
                            circle.makeInVisible()
                            circleSecond.makeInVisible()
                        }
                    }
                }
            }

        class MonthViewContainer(view: View) : ViewContainer(view) {
            val textView = CalendarHeaderBinding.bind(view).exTwoHeaderText
        }

        binding.calendarView.monthHeaderBinder =
            object :
                MonthHeaderFooterBinder<MonthViewContainer> {
                override fun create(view: View) = MonthViewContainer(view)
                override fun bind(container: MonthViewContainer, month: CalendarMonth) {
                    val currentYear = currentDate().dropLast(6).toInt()
                    val toSetMonth = month.yearMonth.month.getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        Locale("ru")
                    ).lowercase().replaceFirstChar { it.uppercase() }

                    container.textView.text = if (month.yearMonth.year != currentYear) {
                        toSetMonth + " ${month.yearMonth.year}"
                    } else {
                        toSetMonth
                    }
                }
            }
    }

    override fun onViewClick() {
        super.onViewClick()

        with(binding) {
            containerChosenDays.setOnClickListener {
                viewModel.getDateOrders(
                    viewModel.firstSelectedDate.toString(),
                    if (viewModel.secondSelectedDate != null) viewModel.secondSelectedDate.toString() else viewModel.firstSelectedDate.toString()
                )
                containerChosenDays.isEnabled = false
            }

            chooseBtn.setOnClickListener {
                setFragmentResult(
                    SELECT_INTERVAL_KEY, bundleOf(
                        SELECT_INTERVAL_BUNDLE to SelectedIntervalData(
                            viewModel.firstSelectedDate,
                            viewModel.secondSelectedDate,
                            viewModel.selectedDayDateType
                        )
                    )
                )
                popBackStack()
            }
        }
    }

    override fun onEach() {

        onEach(viewModel.guidesSchedule) { guidesSchedule ->
            guidesSchedule?.forEach {
                viewModel.scheduleList.add(it)
            }

            if (viewModel.firstTime) {
                initDateRange()
                viewModel.firstTime = false
            }

            with(binding) {

                if (viewModel.setuped)
                    calendarView.updateMonthRange(
                        YearMonth.parse(
                            viewModel.firstDate.dropLast(3)
                        ), YearMonth.parse(viewModel.lastDate.dropLast(3)), removePastDays = true
                    )

                calendarShimmer.makeVisibleGone()
                loadMoreShimmer.makeVisibleGone()

                calendarShimmer.stopShimmer()
                loadMoreShimmer.startShimmer()

                calendarView.makeVisible()
            }

            viewModel.isLoading = false
            viewModel.setuped = true
        }

        onEach(viewModel.dateOrders) {

            binding.containerChosenDays.isEnabled = true
            val bundle = Bundle()
            bundle.putParcelableArrayList(GUIDE_SCHEDULE, it as ArrayList)
            bundle.putParcelable(
                DATES,
                StartEndDate(
                    viewModel.firstSelectedDate ?: LocalDate.MIN,
                    viewModel.secondSelectedDate
                )
            )

            val dialog = EventsOnSelectedDaysBottomSheet()
            dialog.arguments = bundle
            dialog.show(childFragmentManager, "")

            with(dialog) {

                onIndividualItemClick { id, eventId, type ->

                    activity?.supportFragmentManager?.setFragmentResult(
                        NAVIGATE_KEY, bundleOf(
                            NAVIGATE_BUNDLE to
                                    Screen.IndividualOrders.show(
                                        id, eventId, type, true, Gson().toJson(
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
                }

                onGroupItemClick { item ->
                    activity?.supportFragmentManager?.setFragmentResult(
                        NAVIGATE_KEY, bundleOf(
                            NAVIGATE_BUNDLE to
                                    Screen.GroupTourOrders.show(
                                        item.id.toInt(), true, Gson().toJson(
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
                }

                onConfirmOrderClick { id, type, title ->
                    activity?.supportFragmentManager?.setFragmentResult(
                        NAVIGATE_KEY, bundleOf(
                            NAVIGATE_BUNDLE to
                                    Screen.ConfirmOrEdit.show(
                                        id, type, title, true
                                    )
                        )
                    )
                }

                onMessageClick { details ->
                    val orderDetailsForChat =
                        details.copy(avatar = details.avatar.replace("/", " "))

                    activity?.supportFragmentManager?.setFragmentResult(
                        NAVIGATE_KEY, bundleOf(
                            NAVIGATE_BUNDLE to
                                    Screen.Chat.show(
                                        Gson().toJson(orderDetailsForChat),
                                        context?.getString(R.string.date_order_type) ?: "",
                                        Gson().toJson(
                                            viewModel.statisticsData
                                        )
                                    )
                        )
                    )
                }
            }
        }

        onEach(viewModel.guidesScheduleError) {
            viewModel.isLoading = false

            binding.calendarShimmer.makeVisibleGone()
            binding.loadMoreShimmer.makeVisibleGone()

            if (context?.isOnline() == true)
                context?.getStringRes(R.string.no_loading_toast)
                    ?.let {
                        Toast(context).showCustomToast(
                            it,
                            this@CalendarChooseDateFragment,
                            false
                        )
                    } else context?.getStringRes(R.string.no_internet_toast)
                ?.let { Toast(context).showCustomToast(it, this@CalendarChooseDateFragment, false) }
        }
    }

    private fun selectedDaysInterval(dayDate: LocalDate) {

        with(viewModel) {
            when {
                firstSelectedDate != null && secondSelectedDate != null -> {
                    firstSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                    firstSelectedDate = null

                    secondSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                    secondSelectedDate = null

                    firstSelectedDate = dayDate
                    firstSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }

                    viewModel.selectedDays.forEach {
                        binding.calendarView.notifyDateChanged(it)
                    }
                    viewModel.selectedDays.clear()
                    viewModel.showEventButton = false
                }

                dayDate == firstSelectedDate -> {
                    firstSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                    firstSelectedDate = null
                }

                firstSelectedDate != null -> {

                    if (dayDate > firstSelectedDate) {
                        secondSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                        secondSelectedDate = null

                        secondSelectedDate = dayDate
                        secondSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }

                    } else {
                        secondSelectedDate = firstSelectedDate
                        secondSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }

                        firstSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                        firstSelectedDate = null

                        firstSelectedDate = dayDate
                        firstSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                    }

                    viewModel.selectedDays().forEach {
                        binding.calendarView.notifyDateChanged(it)
                    }
                }

                else -> {
                    firstSelectedDate = dayDate
                    firstSelectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                }
            }
        }

        if (viewModel.secondSelectedDate == null) {
            viewModel.selectedDays().forEach {
                binding.calendarView.notifyDateChanged(it)
            }
            viewModel.selectedDays.clear()
        }
        chooseDateBtnVisibilities()
    }

    private fun chooseDateBtnVisibilities() {
        with(binding) {
            chooseBtn.text = context?.let { context ->
                context.getString(
                    R.string.date_or_duration_text,
                    viewModel.firstSelectedDate.selectedDateFormat(
                        context,
                        viewModel.secondSelectedDate,
                        containerChosenDays,
                        containerButtons, eventsTv,
                        viewModel.showEventButton
                    )
                )
            }
        }
    }

    private fun getScheduleDataForAllLoadedDates() {
        val count = viewModel.firstDate.daysCount(viewModel.lastDate)

        for (i in 0..count / Constants.ALLOWED_DAYS_COUNT_FOR_CALENDAR_CALL) {

            if (context?.isOnline() == true)
                viewModel.getGuidesSchedule(
                    viewModel.firstDate.addDays(i * Constants.ALLOWED_DAYS_COUNT_FOR_CALENDAR_CALL),
                    viewModel.firstDate.addDays((i + 1) * Constants.ALLOWED_DAYS_COUNT_FOR_CALENDAR_CALL),
                    null, null
                ) else {
                context?.let {
                    Toast(it).showCustomToast(it.getString(R.string.no_internet_toast), this, false)
                }
            }
        }
    }

    private fun initDateRange() {
        with(viewModel) {
            firstSelectedDate = args.startDate.convertStringToLocalDate()
            secondSelectedDate = args.endDate.convertStringToLocalDate()
            selectedDays()
        }

        chooseDateBtnVisibilities()
    }


    private fun initMonths(begin: String, end: String) {
        val daysOfWeek = daysOfWeekFromLocale()

        binding.calendarView.setup(
            YearMonth.parse(begin.dropLast(3)),
            YearMonth.parse(end.dropLast(3)),
            daysOfWeek.first(),
            begin,
            fromDateFilter = false,
            removePastDays = true
        )
    }

    companion object {
        const val SELECT_INTERVAL_KEY = "SELECT_INTERVAL_KEY"
        const val SELECT_INTERVAL_BUNDLE = "SELECT_INTERVAL_BUNDLE"
        const val GUIDE_SCHEDULE = "GUIDE_SCHEDULE"
        const val DATES = "DATES"
    }
}