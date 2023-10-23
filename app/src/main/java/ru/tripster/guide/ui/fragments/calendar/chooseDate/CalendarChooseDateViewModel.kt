package ru.tripster.guide.ui.fragments.calendar.chooseDate

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.statistics.StatisticsUseCase
import ru.tripster.domain.interactors.сalendar.GetGuidesScheduleUseCase
import ru.tripster.domain.model.calendar.CalendarDayType
import ru.tripster.domain.model.calendar.GuidesSchedule
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.extensions.calcMonthForFullMonth
import ru.tripster.guide.extensions.currentDate
import java.time.LocalDate

class CalendarChooseDateViewModel(
    private val getGuidesScheduleUseCase: GetGuidesScheduleUseCase,
    private val statistics: StatisticsUseCase,
) :
    BaseViewModel() {

    private val _guidesSchedule = MutableStateFlow<List<GuidesSchedule>?>(null)
    val guidesSchedule = _guidesSchedule.asSharedFlow()

    private val _guidesScheduleError: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val guidesScheduleError = _guidesScheduleError.asSharedFlow()

    private val _dateOrders: MutableSharedFlow<List<GuidesSchedule>> by lazy { MutableSharedFlow() }
    val dateOrders = _dateOrders.asSharedFlow()

    private val _statisticsData: MutableSharedFlow<StatisticsData> by lazy { MutableSharedFlow() }
    var statisticsData = StatisticsData.empty()

    private val _errorDateOrderInfo: MutableSharedFlow<Boolean?> by lazy { MutableSharedFlow() }
    val errorDateOrderInfo = _errorDateOrderInfo.asSharedFlow()

    var scheduleList = mutableListOf<GuidesSchedule>()
    var selectedDays = mutableListOf<LocalDate>()
    var isLoading = false
    var setuped = false
    var firstDate = currentDate()
    var lastDate = ""
    var lastCallStartDate = firstDate
    var lastCallEndDate = lastDate
    var firstSelectedDate: LocalDate? = null
    var secondSelectedDate: LocalDate? = null
    var selectedDayDateType: CalendarDayType? = null
    var ordersRateValue: Int = 0
    var bookingRateValue: Int = 0
    var confirmRateValue: Int = 0
    var canOpenContacts: Boolean = false
    var experienceId: Int? = 0
    var showEventButton: Boolean = false
    var firstTime: Boolean = true

    fun getDateOrders(startDate: String, endDate: String) {
        viewModelScope.launch {

            when (val result =
                getGuidesScheduleUseCase.invoke(
                    begin = startDate,
                    end = endDate,
                    null,
                    null,
                    showEventData = true
                )) {
                is ActionResult.Success -> {
                    _dateOrders.emit(result.result)
                }

                is ActionResult.Error -> {

                }
            }
        }
    }

    fun getStatistics() {
        viewModelScope.launch {
            statistics().run {
                (this as? ActionResult.Success)?.let {
                    _statisticsData.emit(StatisticsData.from(it.result))
                }
                (this as? ActionResult.Error)?.let {

                }
            }
        }
    }

    fun getGuidesSchedule(
        begin: String,
        end: String,
        experienceId: Int?,
        experienceTitle: String?
    ) {
        viewModelScope.launch {
            when (val result =
                getGuidesScheduleUseCase(begin, end, experienceId, experienceTitle, null)) {
                is ActionResult.Success -> {
                    _guidesSchedule.emit(result.result)
                }
                is ActionResult.Error -> {
                    _guidesScheduleError.emit(true)
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun selectedDays(): List<LocalDate> {
        var currentDate = firstSelectedDate ?: LocalDate.MIN

        if (secondSelectedDate != null)
            while (currentDate <= secondSelectedDate) {
                scheduleList.forEach {
                    if (it.date == currentDate.toString() && it.guideSchedule.isNotEmpty() && !showEventButton)
                        showEventButton = true

                }
                selectedDays.add(currentDate)
                currentDate = currentDate.plusDays(1)
            } else {
            scheduleList.forEach {
                if (it.date == firstSelectedDate.toString() && it.guideSchedule.isNotEmpty() && !showEventButton)
                    showEventButton = true
            }
        }

        return selectedDays
    }

}