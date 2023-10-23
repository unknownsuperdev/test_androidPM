package ru.tripster.guide.ui.fragments.calendar

import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.calendar.CalendarDayType
import ru.tripster.domain.model.calendar.GuidesSchedule
import ru.tripster.domain.model.calendar.SelectedIntervalData
import ru.tripster.domain.model.calendar.filtering.ExperienceResults
import ru.tripster.guide.Constants.ALLOWED_DAYS_COUNT_FOR_CALENDAR_CALL
import ru.tripster.guide.R
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_CLOSE_TIME
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_DAY_TAP
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_EXPERIENCE_FILTER
import ru.tripster.guide.appbase.CalendarScrollListener
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.CalendarDayBinding
import ru.tripster.guide.databinding.CalendarHeaderBinding
import ru.tripster.guide.databinding.FragmentOrderCalendarBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.calendar.model.CalendarDay
import ru.tripster.guide.ui.calendar.model.CalendarMonth
import ru.tripster.guide.ui.calendar.model.DayOwner
import ru.tripster.guide.ui.calendar.ui.DayBinder
import ru.tripster.guide.ui.calendar.ui.MonthHeaderFooterBinder
import ru.tripster.guide.ui.calendar.ui.ViewContainer
import ru.tripster.guide.ui.calendar.utils.daysOfWeekFromLocale
import ru.tripster.guide.ui.fragments.calendar.CalendarFiltrationBottomSheet.Companion.ORDER_ID_BUNDLE
import ru.tripster.guide.ui.fragments.calendar.CalendarFiltrationBottomSheet.Companion.ORDER_ID_REQUEST_KEY
import ru.tripster.guide.ui.fragments.calendar.closeTime.CalendarCloseTimeFragment
import ru.tripster.guide.ui.fragments.calendar.orders.CalendarDateOrderFragment
import ru.tripster.guide.ui.fragments.calendar.orders.CalendarDateOrderFragment.Companion.CLOSED_TIME_IS_DELETED
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)

class CalendarOrderFragment :
    FragmentBaseNCMVVM<CalendarOrderViewModel, FragmentOrderCalendarBinding>() {

    override val viewModel: CalendarOrderViewModel by viewModel()
    override val binding: FragmentOrderCalendarBinding by viewBinding()
    private val today = LocalDate.now()
    private var busyDays: Array<String>? = null
    private var scheduleList = mutableListOf<GuidesSchedule>()
    override fun onView() {
        activity?.setLightStatusBar()
        activity?.bottomNavBarVisibility(true)

        binding.motionLayout.progress = if (viewModel.currentProgress >= 0.5f) 1f else 0f

        context?.let { viewModel.menuItemClicked(it) }
        initMonths(viewModel.firstDate, viewModel.lastDate)
        getScheduleDataForAllLoadedDates()

        if (context?.isOnline() == true) {
            viewModel.getCalendarFilterData()
        } else errorMessage(false)

        setExperienceTitle(viewModel.experienceTitle)

        setFragmentResultListener(ORDER_ID_REQUEST_KEY) { _, bundle ->
            val result = bundle.getParcelable<ExperienceResults>(ORDER_ID_BUNDLE)
            viewModel.experienceId = result?.id
            viewModel.experienceTitle = if (result?.id == 0) "" else result?.title ?: ""

            binding.loading.makeVisible()

            result?.let {
                setExperienceTitle(result.title)
            }

            scheduleList.clear()

            getScheduleDataForAllLoadedDates()
        }

        setFragmentResultListener(CalendarDateOrderFragment.EXPERIENCE_ID_REQUEST_KEY) { _, bundle ->
            val result =
                bundle.getParcelable<ExperienceResults>(CalendarDateOrderFragment.EXPERIENCE_ID_BUNDLE)

            val isDeleted = bundle.getBoolean(CLOSED_TIME_IS_DELETED)

            if (result?.id != viewModel.experienceId && result != null) {
                viewModel.experienceId = result.id
                binding.loading.makeVisible()
                getScheduleDataForAllLoadedDates()
            }

            if (viewModel.experienceId == 0) {
                viewModel.experienceTitle = ""
                binding.redDot.makeVisibleGone()
                binding.filterText.text = context?.getStringRes(R.string.all_orders)
            } else {
                viewModel.experienceTitle = result?.title ?: viewModel.experienceTitle
                binding.redDot.makeVisible()
                binding.filterText.text = viewModel.experienceTitle
            }

            if (isDeleted) {
                binding.loading.makeVisible()
                getScheduleDataForAllLoadedDates()
            }
        }

        setFragmentResultListener(CalendarCloseTimeFragment.EXPERIENCE_RESULT_KEY) { _, _ ->

            viewModel.getGuidesSchedule(
                viewModel.firstDate,
                viewModel.lastDate,
                viewModel.experienceId,
                viewModel.experienceTitle
            )

            setExperienceTitle(viewModel.experienceTitle)

        }

        setFragmentResultListener(CalendarCloseTimeFragment.TIME_CLOSED_KEY) { _, bundle ->
            val result =
                bundle.getParcelable<ExperienceResults>(CalendarCloseTimeFragment.TIME_CLOSED_BUNDLE)

            binding.loading.makeVisible()

            viewModel.experienceId = result?.id ?: 0
            viewModel.experienceTitle = result?.title ?: ""

            viewModel.getGuidesSchedule(
                viewModel.firstDate,
                viewModel.lastDate,
                viewModel.experienceId,
                viewModel.experienceTitle
            )

            setExperienceTitle(viewModel.experienceTitle)

        }

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
                            viewModel.experienceId,
                            viewModel.experienceTitle
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



        binding.calendarView.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.bindingDay.day
                val circle = container.bindingDay.circle
                val circleSecond = container.bindingDay.circleSecond
                val containerDay = container.bindingDay.container
                textView.text = day.date.dayOfMonth.toString()
                containerDay.isEnabled = false

                containerDay.setOnClickListener {
                    context?.let { context ->
                        viewModel.calendarItemClicked(
                            context,
                            CALENDAR_DAY_TAP
                        )
                    }

                    viewModel.navigateDate = day.date.toString()
                    navigateFragment(
                        CalendarOrderFragmentDirections.actionCalendarOrderFragmentToCalendarDateOrderFragment(
                            day.date.toString(),
                            viewModel.experienceId ?: 0,
                            viewModel.experienceTitle
                        )
                    )
                }

                if (!busyDays.isNullOrEmpty()) {

                    busyDays?.forEach { busyDays ->
                        var numberOfDays = 6L
                        if (day.date == today && day.date.toString() == busyDays) {
                            textView.typeface =
                                resources.getFont(R.font.roboto_bold_700)
                        }

                        if (day.owner == DayOwner.THIS_MONTH) {
                            if (busyDays == day.date.toString()) {
                                containerDay.isEnabled = true
                                textView.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.gray_20
                                    )
                                )
                                while (numberOfDays != 0L) {
                                    if (today.minusDays(numberOfDays).toString() == busyDays) {
                                        textView.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.gray_70
                                            )
                                        )
                                        containerDay.isEnabled = false
                                    }
                                    numberOfDays--
                                }
                            }
                        }

                    }
                }

                onEach(viewModel.guidesSchedule) {

                    if (day.owner == DayOwner.PREVIOUS_MONTH) textView.makeVisibleGone() else textView.makeVisible()
                    if (day.owner == DayOwner.NEXT_MONTH) textView.makeVisibleGone() else textView.makeVisible()

                    if (day.owner == DayOwner.THIS_MONTH) {
                        textView.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.gray_70
                            )
                        )

                        scheduleList.forEach {

                            if (it.date == day.date.toString()) {
                                containerDay.isEnabled = true
                                when (it.dayType) {
                                    CalendarDayType.CLOSED_DAY -> {
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
                                                R.color.gray_20
                                            )
                                        )
                                        circle.makeInVisible()
                                        circleSecond.makeVisibleGone()
                                    }
                                    CalendarDayType.CLOSED_TIME -> {
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
                                containerDay.background = null
                                if (day.date == today) {
                                    textView.typeface =
                                        resources.getFont(R.font.roboto_bold_700)
                                } else {
                                    textView.typeface =
                                        resources.getFont(R.font.roboto_regular_400)
                                }
                            }
                        }
                    } else {
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
        with(binding) {

            filterBtn.setOnClickListener {
                context?.let { context ->
                    viewModel.calendarItemClicked(
                        context,
                        CALENDAR_EXPERIENCE_FILTER
                    )
                }
                navigateFragment(
                    CalendarOrderFragmentDirections.actionCalendarOrderFragmentToCalendarFiltrationBottomSheet(
                        context?.getString(R.string.calendar_type) ?: ""
                    )
                )
            }

            filterText.setOnClickListener {
                context?.let { context ->
                    viewModel.calendarItemClicked(
                        context,
                        CALENDAR_EXPERIENCE_FILTER
                    )
                }

                navigateFragment(
                    CalendarOrderFragmentDirections.actionCalendarOrderFragmentToCalendarFiltrationBottomSheet(
                        context?.getString(R.string.calendar_type) ?: ""
                    )
                )
            }
            closeTimeBtn.setOnClickListener {
                context?.let { context ->
                    viewModel.calendarItemClicked(
                        context,
                        CALENDAR_CLOSE_TIME
                    )
                }

                navigateFragment(
                    CalendarOrderFragmentDirections.actionCalendarOrderFragmentToCalendarCloseTimeFragment(
                        SelectedIntervalData(null, null, null)
                    )
                )
            }

            closeTimeText.setOnClickListener {
                context?.let { context ->
                    viewModel.calendarItemClicked(
                        context,
                        CALENDAR_CLOSE_TIME
                    )
                }

                navigateFragment(
                    CalendarOrderFragmentDirections.actionCalendarOrderFragmentToCalendarCloseTimeFragment(
                        SelectedIntervalData(null, null, null)
                    )
                )
            }

            error.update.setOnClickListener {
                isOnlineAgain()
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.guidesSchedule) { guidesSchedule ->
            binding.loading.makeVisibleGone()

            guidesSchedule?.forEach {
                scheduleList.add(it)
            }

            with(binding) {
                calendarView.updateMonthRange(
                    YearMonth.parse(
                        viewModel.firstDate.dropLast(3)
                    ), YearMonth.parse(viewModel.lastDate.dropLast(3)), false
                )

                calendarShimmer.makeVisibleGone()
                loadMoreShimmer.makeVisibleGone()
                loadMoreShimmer.startShimmer()

                calendarView.makeVisible()
            }

            viewModel.isLoading = false
            viewModel.firstCall = false
        }

        onEach(viewModel.calendarFilterData) {
            viewModel.experienceCount = it.count
        }

        onEach(viewModel.guidesScheduleError) {
            binding.loading.makeVisibleGone()

            if (context?.isOnline() == true) {
                viewModel.isLoading = false
                if (viewModel.firstCall) {
                    errorMessage(true)
                    viewModel.firstCall = false
                } else context?.let {
                    Toast(it).showCustomToast(it.getString(R.string.no_loading_toast), this, false)
                }
            }
        }
    }

    private fun setExperienceTitle(title: String) {
        if (viewModel.experienceId == 0) {
            binding.redDot.makeVisibleGone()
            binding.filterText.text = context?.getStringRes(R.string.all_orders)
        } else {
            binding.redDot.makeVisible()
            binding.filterText.text = title
        }
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

    private fun errorMessage(isOnline: Boolean) {
        with(binding) {
            calendarShimmer.makeVisibleGone()
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

    private fun getScheduleDataForAllLoadedDates() {
        val count = viewModel.firstDate.daysCount(viewModel.lastDate)

        for (i in 0..count / ALLOWED_DAYS_COUNT_FOR_CALENDAR_CALL) {

            if (context?.isOnline() == true)
                viewModel.getGuidesSchedule(
                    viewModel.firstDate.addDays(i * ALLOWED_DAYS_COUNT_FOR_CALENDAR_CALL),
                    viewModel.firstDate.addDays((i + 1) * ALLOWED_DAYS_COUNT_FOR_CALENDAR_CALL),
                    viewModel.experienceId,
                    viewModel.experienceTitle
                ) else {
                binding.loading.makeVisibleGone()
                context?.let {
                    Toast(it).showCustomToast(it.getString(R.string.no_internet_toast), this, false)
                }
            }
        }
    }

    private fun isOnlineAgain() {
        with(binding) {
            if (context?.isOnline() == true) {
                containerError.makeVisibleGone()
                calendarShimmer.makeVisible()
                calendarShimmer.startShimmer()
                calendarView.makeVisibleGone()
                loadMoreShimmer.makeVisibleGone()

                viewModel.getCalendarFilterData()
                viewModel.getGuidesSchedule(
                    viewModel.firstDate,
                    viewModel.lastDate,
                    viewModel.experienceId,
                    viewModel.experienceTitle
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()

        viewModel.currentProgress = binding.motionLayout.progress
    }

    class DayViewContainer(view: View) : ViewContainer(view) {
        // Will be set when this container is bound. See the dayBinder.
        lateinit var day: CalendarDay
        val bindingDay = CalendarDayBinding.bind(view)

    }

}