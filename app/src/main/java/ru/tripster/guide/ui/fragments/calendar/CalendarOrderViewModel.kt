package ru.tripster.guide.ui.fragments.calendar

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.сalendar.ExperienceFullDataUseCase
import ru.tripster.domain.interactors.сalendar.GetGuidesScheduleUseCase
import ru.tripster.domain.model.MenuItems
import ru.tripster.domain.model.calendar.GuidesSchedule
import ru.tripster.domain.model.calendar.filtering.Experience
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.AnalyticsConstants.CALENDAR_MENU_BUTTON
import ru.tripster.guide.analytics.CalendarOrderClicked
import ru.tripster.guide.analytics.MenuItemClicked
import ru.tripster.guide.analytics.UserOrderGroupTourDetails
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.extensions.*

class CalendarOrderViewModel(
    private val getGuidesScheduleUseCase: GetGuidesScheduleUseCase,
    private val experienceFullDataUseCase: ExperienceFullDataUseCase,
    private val analytic: Analytic,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getGuideIdUseCase: GetGuideIdUseCase
) :
    BaseViewModel() {

    private val _guidesSchedule = MutableSharedFlow<List<GuidesSchedule>?>(1)
    val guidesSchedule = _guidesSchedule.asSharedFlow()

    private val _guidesScheduleError = MutableSharedFlow<String?>()
    val guidesScheduleError = _guidesScheduleError.asSharedFlow()

    private val _calendarFilterData: MutableSharedFlow<Experience> by lazy { MutableSharedFlow() }
    val calendarFilterData = _calendarFilterData.asSharedFlow()

    private var monthCount = 4
    var currentProgress = 0.0f
    var isLoading = false
    var firstDate = currentDate()
    var lastDate = currentDate().calcMonthForFullMonth(monthCount)
    var lastCallStartDate = firstDate
    var lastCallEndDate = lastDate
    var experienceId: Int? = 0
    var experienceCount = 0
    var experienceTitle = ""
    var navigateDate = ""
    var firstCall = true
    private var errorToastShown = false
    var motionBundle : Bundle? = null

    init {
//        getGuidesSchedule(firstDate, lastDate, experienceId, experienceTitle)
    }

    fun getGuidesSchedule(
        begin: String,
        end: String,
        experienceId: Int?,
        experienceTitle: String
    ) {
        viewModelScope.launch {

            when (val result =
                getGuidesScheduleUseCase(begin, end, experienceId, experienceTitle, null)) {
                is ActionResult.Success -> {
                    _guidesSchedule.emit(result.result)
                    errorToastShown = false
                }
                is ActionResult.Error -> {
                    if (!errorToastShown) {
                        _guidesScheduleError.emit(result.errors.message ?: "")
                        errorToastShown = true
                    }

//                    getGuidesSchedule(
//                        lastCallStartDate,
//                        lastCallEndDate,
//                        experienceId,
//                        experienceTitle
//                    )
                }
            }
        }
    }

    fun getCalendarFilterData() {
        viewModelScope.launch {
            when (val result = experienceFullDataUseCase.invoke(true)) {
                is ActionResult.Success -> {
                    _calendarFilterData.emit(result.result)
                }
                is ActionResult.Error -> {
                    _guidesScheduleError.emit(result.errors.message)
                }
            }
        }
    }

    fun menuItemClicked(context: Context) {
        viewModelScope.launch {
            analytic.send(
                MenuItemClicked(
                    name = CALENDAR_MENU_BUTTON,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    menuButton = MenuItems.CALENDAR.type
                )
            )
        }
    }
    fun calendarItemClicked(context: Context, name:String) {
        viewModelScope.launch {
            analytic.send(
                CalendarOrderClicked(
                    name = name,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0
                )
            )
        }
    }
}