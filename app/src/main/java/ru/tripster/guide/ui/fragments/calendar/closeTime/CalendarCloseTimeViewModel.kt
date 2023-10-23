package ru.tripster.guide.ui.fragments.calendar.closeTime

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.core.CallException
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.сalendar.*
import ru.tripster.domain.model.calendar.CloseTimeSchedule
import ru.tripster.domain.model.calendar.GuidesSchedule
import ru.tripster.domain.model.calendar.SelectedIntervalData
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.AnalyticsConstants.CLOSE_TIME_TAP
import ru.tripster.guide.analytics.CalendarOrderClicked
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class CalendarCloseTimeViewModel(
    private val setExperienceIdUseCase: SetExperienceIdUseCase,
    private val getExperienceTitleUseCase: GetExperienceTitleUseCase,
    private val getExperienceIdUseCase: GetExperienceIdUseCase,
    private val getGuidesScheduleUseCase: GetGuidesScheduleUseCase,
    private val closeTimeUseCase: CloseTimeUseCase,
    private val analytic: Analytic,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getGuideIdUseCase: GetGuideIdUseCase
) : BaseViewModel() {

    private val _savedId: MutableStateFlow<Unit?> by lazy { MutableStateFlow(null) }

    private val _savedTitle = MutableStateFlow("")
    val savedTitle = _savedTitle.asSharedFlow()

    private val _guidesSchedule = MutableStateFlow<List<GuidesSchedule>?>(null)
    val guidesSchedule = _guidesSchedule.asSharedFlow()

    private val _addBusySchedule = MutableStateFlow<Boolean?>(null)
    val addBusySchedule = _addBusySchedule.asStateFlow()

    private val _addBusyScheduleError = MutableSharedFlow<String?>()
    val addBusyScheduleError = _addBusyScheduleError.asSharedFlow()

    var haveEvent = ""
    var experienceId: Int = 0
    var experienceTitle = ""
    var isBusyAllDay = false
    var addBusyTime: CloseTimeSchedule? = null
    var closedTimeDesc = ""
    var isSelectedDay = true
    var selectedData: SelectedIntervalData? = null
    var startTime = ""
    var endTime = ""
    var isStartTimeSelected = false
    var firstTime = true

    init {
        getExperienceTitle()
        viewModelScope.launch {
            saveExperienceId(getExperienceIdUseCase().first() ?: 0)
        }
    }

    fun getExperienceTitle() {
        viewModelScope.launch {
            getExperienceTitleUseCase().first()?.let {
                _savedTitle.emit(it.ifEmpty {
                    CalendarFilterFirstItem.FROM_CLOSE_TIME.value
                })
            }
        }
    }

    fun saveExperienceId(experienceId: Int) {
        viewModelScope.launch {
            _savedId.emit(setExperienceIdUseCase(experienceId))
            this@CalendarCloseTimeViewModel.experienceId = experienceId
        }
    }

    fun addBusySchedule(
        closeTimeSchedule: CloseTimeSchedule,
        experienceId: Int,
        experienceTitle: String
    ) {
        viewModelScope.launch {
            when (val result = closeTimeUseCase(closeTimeSchedule, experienceId, experienceTitle)) {
                is ActionResult.Success -> {
                    _addBusySchedule.emit(true)
                }

                is ActionResult.Error -> {
                    _addBusyScheduleError.emit(((result.errors) as CallException).errorMessage)
                }
            }
        }
    }

    fun getGuidesSchedule(begin: String, end: String, experienceId: Int?) {
        viewModelScope.launch {
            when (val result =
                getGuidesScheduleUseCase(begin, end, experienceId, experienceTitle, null)) {
                is ActionResult.Success -> {
                    _guidesSchedule.emit(result.result)
                }
                is ActionResult.Error -> {

                }
            }
        }
    }

    fun closeTimeClicked(context: Context) {
        viewModelScope.launch {
            analytic.send(
                CalendarOrderClicked(
                    name = CLOSE_TIME_TAP,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0
                )
            )
        }
    }

}