package ru.tripster.guide.ui.calendar

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.сalendar.GetGuidesScheduleUseCase
import ru.tripster.domain.model.calendar.GuidesSchedule
import ru.tripster.guide.appbase.viewmodel.BaseViewModel

class CalendarViewModel(private val getGuidesScheduleUseCase: GetGuidesScheduleUseCase) :
    BaseViewModel() {
    private val _guidesSchedule = MutableStateFlow<List<GuidesSchedule>?>(null)
    val guidesSchedule = _guidesSchedule.asSharedFlow()

    private val _dismiss = MutableSharedFlow<Unit>()
    val dismiss = _dismiss

    fun dismiss() {
        viewModelScope.launch {
            delay(20)
            _dismiss.emit(Unit)
        }
    }

    fun getGuidesSchedule(begin: String, end: String, experienceId: Int?) {
        viewModelScope.launch {
            when (val result = getGuidesScheduleUseCase(begin, end, experienceId, null,null)) {
                is ActionResult.Success -> {
                    _guidesSchedule.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO:Handler
                }
            }
        }
    }
}
