package ru.tripster.guide.ui.calendar

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.View
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.calendar.CalendarDayType
import ru.tripster.guide.R
import ru.tripster.guide.appbase.bottomSheetDialog.BottomSheetDialogFragmentBaseMVVM
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.CalendarDayBinding
import ru.tripster.guide.databinding.CalendarHeaderBinding
import ru.tripster.guide.databinding.FragmentCalendarBinding
import ru.tripster.guide.extensions.calcMonth
import ru.tripster.guide.extensions.makeInVisible
import ru.tripster.guide.extensions.makeVisible
import ru.tripster.guide.extensions.makeVisibleGone
import ru.tripster.guide.ui.calendar.model.CalendarDay
import ru.tripster.guide.ui.calendar.model.CalendarMonth
import ru.tripster.guide.ui.calendar.model.DayOwner
import ru.tripster.guide.ui.calendar.ui.DayBinder
import ru.tripster.guide.ui.calendar.ui.MonthHeaderFooterBinder
import ru.tripster.guide.ui.calendar.ui.ViewContainer
import ru.tripster.guide.ui.calendar.utils.daysOfWeekFromLocale
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
class CalendarBottomSheet :
    BottomSheetDialogFragmentBaseMVVM<CalendarViewModel, FragmentCalendarBinding>() {

    override val viewModel: CalendarViewModel by viewModel()
    override val binding: FragmentCalendarBinding by viewBinding()
    private val args: CalendarBottomSheetArgs by navArgs()

    private var selectedDate: LocalDate? = null
    private val today = LocalDate.now()
    private var busyDays: Array<String>? = null
    private var selectAnotherDate: (() -> Unit)? = null


    override fun onViewClick() {
        binding.closeBtn.setOnClickListener {
            dismiss()
        }
    }

    override fun onView() {
        dialog?.setOnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val coordinatorLayout = binding.container
            val bottomSheetInternal = d.findViewById<View>(R.id.bottomSheet) as ConstraintLayout
            val bottomSheetBehavior = BottomSheetBehavior.from<View>(bottomSheetInternal)
            BottomSheetBehavior.from(coordinatorLayout.parent as View).peekHeight =
                bottomSheetInternal.height
            bottomSheetBehavior.peekHeight = bottomSheetInternal.height
            coordinatorLayout.parent.requestLayout()
        }

        initMonths()
        class DayViewContainer(view: View) : ViewContainer(view) {
            // Will be set when this container is bound. See the dayBinder.
            lateinit var day: CalendarDay
            val bindingDay = CalendarDayBinding.bind(view)

            init {
                view.setOnClickListener {

                    if (day.owner == DayOwner.THIS_MONTH) {
                        selectedDate = day.date
                        selectedDate?.let { binding.calendarView.notifyDateChanged(it) }
                    }
                }
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
                containerDay.isClickable = false

                if (args.selectedDate != "" && day.owner == DayOwner.THIS_MONTH) {
                    if (args.selectedDate == day.date.toString()) {
                        containerDay.background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.calendar_item_selected
                        )
                        selectAnotherDate = {
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
                    }
                }

                if (!busyDays.isNullOrEmpty()) {
                    if (day.owner == DayOwner.PREVIOUS_MONTH) textView.makeVisibleGone()
                    if (day.owner == DayOwner.NEXT_MONTH) textView.makeVisibleGone()
                    busyDays?.forEach { busyDays ->
                        var numberOfDays = 6L
                        if (day.date == today && day.date.toString() == busyDays) {
                            textView.typeface =
                                resources.getFont(R.font.roboto_bold_700)
                        }
                        if (day.owner == DayOwner.THIS_MONTH) {
                            if (busyDays == day.date.toString()) {
                                containerDay.isClickable = true
                                if (busyDays == args.selectedDate && args.selectedDate != "") {
                                    textView.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.white
                                        )
                                    )
                                } else {
                                    textView.setTextColor(
                                        ContextCompat.getColor(
                                            requireContext(),
                                            R.color.gray_20
                                        )
                                    )
                                }
                                while (numberOfDays != 0L) {
                                    if (today.minusDays(numberOfDays).toString() == busyDays) {
                                        textView.setTextColor(
                                            ContextCompat.getColor(
                                                requireContext(),
                                                R.color.gray_70
                                            )
                                        )
                                        containerDay.isClickable = false
                                    }
                                    numberOfDays--
                                }
                            }
                        }
                        when (day.date) {
                            selectedDate -> {
                                selectAnotherDate?.invoke()
                                containerDay.background = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.calendar_item_selected
                                )
                                textView.setTextColor(
                                    ContextCompat.getColor(
                                        requireContext(),
                                        R.color.white
                                    )
                                )
                                setFragmentResult(
                                    CLOSED_DAY_BUNDLE,
                                    bundleOf(
                                        CLOSED_DAY_REQUEST_KEY to selectedDate.toString(),
                                    )
                                )
                                viewModel.dismiss()
                            }
                            else -> if (args.selectedDate == "") containerDay.background =
                                null
                        }

                    }
                }

                onEach(viewModel.dismiss) {
                    dismiss()
                }

                onEach(viewModel.guidesSchedule) { guidesSchedule ->
                    if (day.owner == DayOwner.THIS_MONTH) {
                        when (day.date) {
                            selectedDate -> {
                                selectAnotherDate?.invoke()
                                guidesSchedule?.forEach {
                                    if (it.date == day.date.toString()) {
                                        setFragmentResult(
                                            CHOOSE_DATE_BUNDLE,
                                            bundleOf(
                                                CHOOSE_DATE_REQUEST_KEY to selectedDate.toString(),
                                                CLOSED_TIME_REQUEST_KEY to it.guideSchedule
                                            )
                                        )
                                        viewModel.dismiss()
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
                                containerDay.background = ContextCompat.getDrawable(
                                    requireContext(),
                                    R.drawable.calendar_item_selected
                                )
                                if (selectedDate == today) {
                                    textView.typeface = resources.getFont(R.font.roboto_bold_700)
                                }
                            }
                            else -> {
                                guidesSchedule?.forEach {
                                    if (it.date == day.date.toString()) {
                                        containerDay.isClickable = true
                                        when (it.dayType) {
                                            CalendarDayType.CLOSED_DAY -> {
                                                if (args.selectedDate != "" && day.date.toString() == args.selectedDate) {
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
                                                } else {
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

                                            }
                                            CalendarDayType.HAVE_RESERVATION -> {
                                                if (args.selectedDate != "" && day.date.toString() == args.selectedDate) {
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
                                                } else {
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
                                            }
                                            CalendarDayType.FREE_DAY -> {
                                                if (args.selectedDate != "" && day.date.toString() == args.selectedDate) {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.white
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    circleSecond.makeVisibleGone()
                                                } else {
                                                    textView.setTextColor(
                                                        ContextCompat.getColor(
                                                            requireContext(),
                                                            R.color.gray_20
                                                        )
                                                    )
                                                    circle.makeVisibleGone()
                                                    circleSecond.makeVisibleGone()
                                                }
                                            }
                                            CalendarDayType.CLOSED_TIME -> {
                                                if (args.selectedDate != "" && day.date.toString() == args.selectedDate) {
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
                                                } else {
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
                                            }
                                            CalendarDayType.BOOKED_ORDER_TIME_CLOSED -> {
                                                if (args.selectedDate != "" && day.date.toString() == args.selectedDate) {
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
                                                } else {
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
                                }
                                if (args.selectedDate == "") containerDay.background = null
                                if (day.date == today) {
                                    textView.typeface = resources.getFont(R.font.roboto_bold_700)
                                }
                            }
                        }
                    } else {
                        textView.makeInVisible()
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

    private fun initMonths() {
        val begin = currentDate().dropLast(3)
        val monthCount = 5
        if (args.type != "cancel") {
            viewModel.getGuidesSchedule(begin.calcMonth(0), begin.calcMonth(monthCount), null)
        } else {
            binding.title.text = context?.resources?.getString(R.string.select_date)
            busyDays = args.busyDays
        }

        val daysOfWeek = daysOfWeekFromLocale()

        monthCount.let { YearMonth.parse(begin).plusMonths(it.toLong()) }?.let {
            binding.calendarView.setup(
                YearMonth.parse(begin),
                it,
                daysOfWeek.first(),
                currentDate(),
                fromDateFilter = false,
                true
            )
        }
    }

    private fun currentDate(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(Calendar.getInstance().time)

    companion object {
        const val CHOOSE_DATE_BUNDLE = "CHOOSE_DATE_BUNDLE"
        const val CHOOSE_DATE_REQUEST_KEY = "CHOOSE_DATE_REQUEST_KEY"
        const val CLOSED_TIME_REQUEST_KEY = "CLOSED_TIME_REQUEST_KEY"
        const val CLOSED_DAY_REQUEST_KEY = "CLOSED_DAY_REQUEST_KEY"
        const val CLOSED_DAY_BUNDLE = "CLOSED_DAY_BUNDLE"
    }
}

