package ru.tripster.guide.ui.fragments.calendar.closeTime

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.Rect
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.addCallback
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.fragment.navArgs
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.tripster.domain.model.calendar.CalendarDayType
import ru.tripster.domain.model.calendar.CloseTimeSchedule
import ru.tripster.domain.model.calendar.filtering.ExperienceResults
import ru.tripster.domain.model.order.ChooseTime
import ru.tripster.guide.Constants.ONE_DAY_IN_MINUTES
import ru.tripster.guide.R
import ru.tripster.guide.Screen
import ru.tripster.guide.appbase.FragmentBaseNCMVVM
import ru.tripster.guide.appbase.utils.bottomNavBarVisibility
import ru.tripster.guide.appbase.utils.viewBinding
import ru.tripster.guide.databinding.FragmentCloseTimeBinding
import ru.tripster.guide.extensions.*
import ru.tripster.guide.ui.MainActivity
import ru.tripster.guide.ui.MainFragment
import ru.tripster.guide.ui.MainFragment.Companion.BOTTOM_NAV_BAR_VISIBILITY_KEY
import ru.tripster.guide.ui.fragments.calendar.CalendarFiltrationBottomSheet
import ru.tripster.guide.ui.fragments.calendar.chooseDate.CalendarChooseDateFragment
import ru.tripster.guide.ui.fragments.confirmOrder.ChooseTimeBottomSheet


class CalendarCloseTimeFragment :
    FragmentBaseNCMVVM<CalendarCloseTimeViewModel, FragmentCloseTimeBinding>() {

    override val viewModel: CalendarCloseTimeViewModel by viewModel()
    override val binding: FragmentCloseTimeBinding by viewBinding()

    val args: CalendarCloseTimeFragmentArgs by navArgs()

    override fun onDestroy() {
        super.onDestroy()
        setFragmentResult(
            EXPERIENCE_RESULT_KEY,
            bundleOf(EXPERIENCE_RESULT_BUNDLE to true)
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity?.onBackPressedDispatcher?.addCallback(this) {
            popBackStack()
        }
    }

    override fun onView() {

        activity?.bottomNavBarVisibility(false)
        setFragmentResultListener(CalendarChooseDateFragment.SELECT_INTERVAL_KEY) { _, bundle ->
            viewModel.selectedData =
                bundle.getParcelable(CalendarChooseDateFragment.SELECT_INTERVAL_BUNDLE)

            binding.selectingFilter.text = viewModel.experienceTitle
            setRangeText()
            setTimeTextLogic()
        }

        with(binding) {
            binding.selectingFilter.text = viewModel.experienceTitle
//            setRangeText()
            setTimeTextLogic()

            toolbar.visibilityOfButtons(false)
            context?.getString(R.string.closing_time)?.let { toolbar.setTitle(it) }
            commentClickLogic()
        }

        if (args.date.firstSelectedDate != null) {
            viewModel.selectedData = args.date
            setRangeText()
        }

        setFragmentResultListener(CalendarFiltrationBottomSheet.EXPERIENCE_KEY) { _, bundle ->
            val result =
                bundle.getParcelable<ExperienceResults>(CalendarFiltrationBottomSheet.EXPERIENCE_BUNDLE)

            binding.selectingFilter.text = result?.title
            if (result != null) {
                viewModel.experienceId = result.id
                viewModel.experienceTitle =
                    if (result.title == CalendarFilterFirstItem.NOT_FROM_CLOSE_TIME.value) CalendarFilterFirstItem.FROM_CLOSE_TIME.name else result.title
            }

            viewModel.saveExperienceId(viewModel.experienceId)
        }

        activity?.supportFragmentManager?.setFragmentResultListener(ChooseTimeBottomSheet.CHOOSE_TIME_REQUEST_KEY,viewLifecycleOwner) { _, bundle ->
            val result = bundle.getParcelable<ChooseTime>(ChooseTimeBottomSheet.CHOOSE_TIME_BUNDLE)

            context?.let {
                if (viewModel.isStartTimeSelected) viewModel.startTime =
                    result?.time ?: it.getString(R.string.start_time) else viewModel.endTime =
                    result?.time ?: it.getString(R.string.start_time)
            }

            setTimeTextLogic()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewClick() {
        with(binding) {
            selectingFilter.setOnClickListener {
                navigateFragment(
                    CalendarCloseTimeFragmentDirections.actionCalendarCloseTimeFragmentToCalendarFiltrationBottomSheet(
                        context?.getString(R.string.closing_time) ?: ""
                    )
                )
            }
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    viewModel.isBusyAllDay = true
                    with(startTime) {
                        text = context?.getString(R.string.start_time)
                        setTextColor(ContextCompat.getColor(context, R.color.gray_70))
                        setBackgroundResource(R.drawable.shape_gray80_90_rectangle_8)
                        setDrawableColor(R.color.gray_70, this)
                        isClickable = false
                    }

                    with(endTime) {
                        text = context?.getString(R.string.end_time)
                        setTextColor(ContextCompat.getColor(context, R.color.gray_70))
                        setBackgroundResource(R.drawable.shape_gray80_90_rectangle_8)
                        setDrawableColor(R.color.gray_70, this)
                        isClickable = false
                    }
                } else {
                    viewModel.isBusyAllDay = false
                    with(startTime) {
                        text = viewModel.startTime
                        setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                        setBackgroundResource(R.drawable.shape_gray80_rectangle_8)
                        setDrawableColor(R.color.gray_20, this)
                        isClickable = true
                    }

                    with(endTime) {
                        text = viewModel.endTime
                        setTextColor(ContextCompat.getColor(context, R.color.gray_20))
                        setBackgroundResource(R.drawable.shape_gray80_rectangle_8)
                        setDrawableColor(R.color.gray_20, this)
                        isClickable = true
                    }
                }
            }

            startTime.setOnClickListener {
                viewModel.isStartTimeSelected = true

                    (activity as MainActivity).navController.navigate(
                        Screen.ChooseTime.show(
                            viewModel.startTime,
                            true,
                            "00:00",
                            viewModel.startTime,
                            viewModel.endTime
                        )
                    )
//                navigateFragment(
//                    CalendarCloseTimeFragmentDirections.actionCalendarCloseTimeFragmentToChooseTimeBottomSheet(
//                        viewModel.startTime,
//                        true,
//                        "00:00",
//                        viewModel.startTime,
//                        viewModel.endTime
//                    )
//                )
            }

            endTime.setOnClickListener {
                viewModel.isStartTimeSelected = false
//                navigateFragment(
//                    CalendarCloseTimeFragmentDirections.actionCalendarCloseTimeFragmentToChooseTimeBottomSheet(
//                        viewModel.endTime,
//                        true,
//                        "00:30",
//                        viewModel.startTime,
//                        viewModel.endTime
//                    )
//                )

                (activity as MainActivity).navController.navigate(
                    Screen.ChooseTime.show(
                        viewModel.endTime,
                        true,
                        "00:30",
                        viewModel.startTime,
                        viewModel.endTime
                    )
                )
            }

            specifyDateOrPeriod.setOnClickListener {
                navigateFragment(
                    CalendarCloseTimeFragmentDirections.actionCalendarCloseTimeFragmentToCalendarChooseDateFragment(
                        viewModel.selectedData?.firstSelectedDate?.toString() ?: "",
                        viewModel.selectedData?.secondSelectedDate?.toString() ?: ""
                    )
                )
            }
            lConst.setOnClickListener {
                activity?.hideSoftKeyboard(comment)
            }
            comment.setOnClickListener {
                activity?.showSoftKeyboard(comment)
            }
            closeTimeBtn.setOnClickListener {
                context?.let { context -> viewModel.closeTimeClicked(context) }

                if (validFields()) {
                    context?.let { context ->
                        viewModel.addBusyTime = CloseTimeSchedule(
                            comment = comment.text.toString().ifEmpty { null },
                            duration = if (!viewModel.isBusyAllDay) viewModel.startTime.countDuration(
                                viewModel.endTime
                            ) else ONE_DAY_IN_MINUTES,
                            endDate = if (viewModel.selectedData?.secondSelectedDate != null) viewModel.selectedData?.secondSelectedDate.toString() else viewModel.selectedData?.firstSelectedDate.toString(),
                            experience = if (viewModel.experienceId != 0) viewModel.experienceId else null,
                            startDate = viewModel.selectedData?.firstSelectedDate.toString(),
                            startTime = if (!viewModel.isBusyAllDay) viewModel.startTime else context.getString(
                                R.string.start_time
                            )
                        )

                        val date = viewModel.selectedData?.firstSelectedDate.selectedDateFormat(
                            context,
                            viewModel.selectedData?.secondSelectedDate,
                            null,
                            null, null,
                            false
                        )

                        viewModel.closedTimeDesc =
                            context.getString(
                                R.string.date_interval_toast,
                                date,
                                if (viewModel.isBusyAllDay) context.getString(R.string.time_interval_all_day) else context.getString(
                                    R.string.time_interval,
                                    viewModel.startTime,
                                    viewModel.endTime
                                )
                            )

                        with(binding) {
                            closeTimeBtn.setTextColor(
                                ContextCompat.getColor(
                                    context,
                                    R.color.teal_500
                                )
                            )
                            progress.makeVisible()
                        }
                    }

                    viewModel.addBusyTime?.let {
                        viewModel.addBusySchedule(
                            it,
                            viewModel.experienceId,
                            viewModel.experienceTitle
                        )
                    }

                } else {
                    comment.clearFocus()
                    specifyDateOrPeriodLogic()
                    startAndEndTimeLogic()
                }
            }
        }
    }

    override fun onEach() {
        onEach(viewModel.addBusySchedule) { busyTime ->
            if (busyTime == true) {
                context?.getString(R.string.closed_time, viewModel.closedTimeDesc)?.let {
                    Toast(context).showCustomToast(it, this@CalendarCloseTimeFragment, true)
                }
                setFragmentResult(
                    TIME_CLOSED_KEY,
                    bundleOf(
                        TIME_CLOSED_BUNDLE to ExperienceResults(
                            "",
                            viewModel.experienceId,
                            true,
                            "",
                            "",
                            viewModel.experienceTitle,
                            "",
                            true
                        )
                    )
                )
                navigateFragment(CalendarCloseTimeFragmentDirections.actionCalendarCloseTimeFragmentToCalendarOrderFragment())
                popBackStack()
            }

            with(binding) {
                context?.let {
                    closeTimeBtn.setTextColor(
                        ContextCompat.getColor(
                            it,
                            R.color.white
                        )
                    )
                }
                progress.makeVisibleGone()
            }
        }

        onEach(viewModel.addBusyScheduleError) { error ->

            error?.let {
                Toast(context).showCustomToast(error, this, false)
            }

            with(binding) {
                context?.let {
                    closeTimeBtn.setTextColor(
                        ContextCompat.getColor(
                            it,
                            R.color.white
                        )
                    )
                }
                progress.makeVisibleGone()
            }

        }

        onEach(viewModel.savedTitle) {
            if (viewModel.firstTime) {
                viewModel.experienceTitle =
                    it.ifEmpty { CalendarFilterFirstItem.FROM_CLOSE_TIME.value }
                binding.selectingFilter.text = viewModel.experienceTitle
                viewModel.firstTime = false
            }
        }

        onEach(viewModel.guidesSchedule) { guideSchedule ->
            if (viewModel.isSelectedDay) {
                viewModel.haveEvent = ""
                binding.haveBookedOrders.text = viewModel.haveEvent
                guideSchedule?.forEach {
                    if (it.dayType == CalendarDayType.BOOKED_ORDER_TIME_CLOSED || it.dayType == CalendarDayType.HAVE_RESERVATION) {
                        viewModel.haveEvent += "${it.date.dateChange().dropLast(5)}, "
                    }
                }

                with(binding) {
                    if (viewModel.haveEvent != "") {
                        icNotify.makeVisible()
                        haveBookedOrders.makeVisible()
                        haveBookedOrders.text =
                            context?.getString(
                                R.string.meetings_are_scheduled,
                                viewModel.haveEvent.dropLast(2)
                            )
                    } else {
                        icNotify.makeVisibleGone()
                        haveBookedOrders.makeVisibleGone()
                    }
                }
            }
        }
    }

    private fun validFields() = specifyDateOrPeriodLogic() && startAndEndTimeLogic()

    private fun specifyDateOrPeriodLogic(): Boolean {
        with(binding) {
            return if (specifyDateOrPeriod.text?.isEmpty() == true) {
                specifyDateOrPeriod.setBackgroundResource(R.drawable.shape_edt_tomat_rectangle_8)
                false
            } else {
                specifyDateOrPeriod.setBackgroundResource(R.drawable.shape_gray80_rectangle_8)
                true
            }
        }
    }

    private fun setDrawableColor(color: Int, textView: AppCompatTextView) {
        context?.let { context ->
            textView.compoundDrawables[2]?.let {
                it.mutate().colorFilter = PorterDuffColorFilter(
                    ContextCompat.getColor(context, color),
                    PorterDuff.Mode.SRC_IN
                )
            }
        }
    }

    private fun setRangeText() {
        binding.specifyDateOrPeriod.text = context?.let { context ->
            viewModel.selectedData?.firstSelectedDate.selectedDateFormat(
                context,
                viewModel.selectedData?.secondSelectedDate,
                null,
                null, null,
                false
            )
        }

        if (viewModel.selectedData?.secondSelectedDate != null) {
            viewModel.isSelectedDay = true
            viewModel.getGuidesSchedule(
                viewModel.selectedData?.firstSelectedDate.toString(),
                viewModel.selectedData?.secondSelectedDate.toString(),
                null
            )
        } else if (viewModel.selectedData?.selectedDayDateType == CalendarDayType.HAVE_RESERVATION || viewModel.selectedData?.selectedDayDateType == CalendarDayType.BOOKED_ORDER_TIME_CLOSED) {
            binding.icNotify.makeVisible()
            binding.haveBookedOrders.makeVisible()
            binding.haveBookedOrders.text = context?.getString(
                R.string.meetings_are_scheduled,
                viewModel.selectedData?.firstSelectedDate.toString().dateChange().dropLast(5)
            )
            viewModel.isSelectedDay = false
        } else {
            binding.icNotify.makeVisibleGone()
            binding.haveBookedOrders.makeVisibleGone()
            viewModel.isSelectedDay = false
        }
    }

    private fun setTimeTextLogic() {
        if (!viewModel.isBusyAllDay) {
            binding.startTime.text = viewModel.startTime
            binding.endTime.text = viewModel.endTime
        } else {
            binding.startTime.text = context?.getString(R.string.start_time)
            binding.endTime.text = context?.getString(R.string.end_time)
        }
    }

    private fun commentClickLogic() {

        with(binding) {
            lConst.viewTreeObserver.addOnGlobalLayoutListener {
                if (isAdded) {
                    val rect = Rect()
                    lConst.getWindowVisibleDisplayFrame(rect)
                    val screenHeight: Int = lConst.rootView.height
                    val keypadHeight = screenHeight - rect.bottom
                    if (keypadHeight > screenHeight * 0.15) {
                        comment.setBackgroundResource(R.drawable.shape_stroke_orange_80)
                        if (Build.VERSION.SDK_INT <= 28) comment.text?.let {
                            comment.setSelection(
                                it.length
                            )
                        }
                        comment.isCursorVisible = true
                    } else {
                        comment.setBackgroundResource(R.drawable.shape_stroke_gray)
                        comment.isCursorVisible = false
                    }
                }
            }
        }
    }

    private fun startAndEndTimeLogic(): Boolean {
        with(binding) {
            when {
                startTime.text.isEmpty() && endTime.text.isEmpty() -> {
                    if (!viewModel.isBusyAllDay) {
                        startTime.setBackgroundResource(R.drawable.shape_edt_tomat_rectangle_8)
                        endTime.setBackgroundResource(R.drawable.shape_edt_tomat_rectangle_8)
                        return false
                    } else return true
                }
                startTime.text.isEmpty() && endTime.text.isNotEmpty() -> {
                    startTime.setBackgroundResource(R.drawable.shape_edt_tomat_rectangle_8)
                    endTime.setBackgroundResource(R.drawable.shape_gray80_rectangle_8)
                    return false
                }
                startTime.text.isNotEmpty() && endTime.text.isEmpty() -> {
                    startTime.setBackgroundResource(R.drawable.shape_gray80_rectangle_8)
                    endTime.setBackgroundResource(R.drawable.shape_edt_tomat_rectangle_8)
                    return false
                }
                else -> {
                    startTime.setBackgroundResource(R.drawable.shape_gray80_rectangle_8)
                    endTime.setBackgroundResource(R.drawable.shape_gray80_rectangle_8)
                    return true
                }

            }
        }
    }

    companion object {
        const val EXPERIENCE_RESULT_KEY = "EXPERIENCE_RESULT_KEY"
        const val EXPERIENCE_RESULT_BUNDLE = "EXPERIENCE_RESULT_BUNDLE"
        const val TIME_CLOSED_KEY = "TIME_CLOSED_KEY"
        const val TIME_CLOSED_BUNDLE = "TIME_CLOSED_BUNDLE"
    }

}