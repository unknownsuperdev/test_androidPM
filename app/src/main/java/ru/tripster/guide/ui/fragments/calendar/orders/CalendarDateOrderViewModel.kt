package ru.tripster.guide.ui.fragments.calendar.orders

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.statistics.StatisticsUseCase
import ru.tripster.domain.interactors.сalendar.BusyIntervalUseCase
import ru.tripster.domain.interactors.сalendar.GetGuideDateScheduleUseCase
import ru.tripster.domain.model.MenuItems
import ru.tripster.domain.model.calendar.CalendarDayType
import ru.tripster.domain.model.calendar.GuideDateSchedule
import ru.tripster.domain.model.calendar.filtering.EventScheduleType
import ru.tripster.domain.model.calendar.filtering.ExperienceResults
import ru.tripster.domain.model.statistics.OrderStatisticsData
import ru.tripster.guide.analytics.*
import ru.tripster.guide.analytics.CalendarDateExperienceIndividual
import ru.tripster.guide.analytics.CalendarOrderClicked
import ru.tripster.guide.analytics.UserTapExperienceGroupTourOrder
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.enums.OrderTypes
import ru.tripster.guide.extensions.statusValueGroupTour
import ru.tripster.guide.extensions.statusValueIndividual
import ru.tripster.guide.extensions.tourType

class CalendarDateOrderViewModel(
    private val getDateOrdersUseCase: GetGuideDateScheduleUseCase,
    private val deleteBusyIntervalUseCase: BusyIntervalUseCase,
    private val statistics: StatisticsUseCase,
    private val analytic: Analytic,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getGuideIdUseCase: GetGuideIdUseCase
) :
    BaseViewModel() {

    private val _dateOrders: MutableSharedFlow<GuideDateSchedule> by lazy { MutableSharedFlow() }
    val dateOrders = _dateOrders.asSharedFlow()

    private val _statistics: MutableSharedFlow<OrderStatisticsData> by lazy { MutableSharedFlow() }
    val statisticsData = _statistics.asSharedFlow()

    private val _errorDateOrderInfo: MutableSharedFlow<Boolean?> by lazy { MutableSharedFlow() }
    val errorDateOrderInfo = _errorDateOrderInfo.asSharedFlow()

    private val _deleteBusyInterval: MutableSharedFlow<Boolean?> by lazy { MutableSharedFlow() }
    val deleteBusyInterval = _deleteBusyInterval.asSharedFlow()


    var ordersRateValue: Int = 0
    var bookingRateValue: Int = 0
    var confirmRateValue: Int = 0
    var canOpenContacts: Boolean = false
    var experienceId: Int? = null
    var argsExperienceId: Int? = null
    var experienceTitle: String = ""
    var itemsCount: Int = 0
    var experienceResult: ExperienceResults? = null
    var showFilterContainer = false
    var isDeleted: Boolean = false
    var isHaveEvent: Boolean = false
    var isCloseTime: Boolean = false
    var bookedOrdersCount = 0
    var firstTime = true
    var setArgsExperienceTitle = true
    var selectedDayDateType: CalendarDayType? = null
    val resultsList = mutableListOf<String>()

    fun getDateOrders(
        date: String,
        experienceId: Int?,
        experienceTitle: String?
    ) {
        viewModelScope.launch {
            getDateOrdersUseCase(date, experienceId, experienceTitle).run {
                (this as? ActionResult.Success)?.let {
                    _dateOrders.emit(result)
                    if (result.experienceId == 0) bookedOrdersCount = result.eventCount
                }
                (this as? ActionResult.Error)?.let {
                    _errorDateOrderInfo.emit(true)
                }
            }
        }
    }

    fun deleteBusyInterval(id: Int) {
        viewModelScope.launch {
            deleteBusyIntervalUseCase.invoke(id).run {
                (this as? ActionResult.Success)?.let {
                    _deleteBusyInterval.emit(result)
                }
                (this as? ActionResult.Error)?.let {

                }
            }
        }
    }

    fun getStatistics() {
        viewModelScope.launch {
            statistics().run {
                (this as? ActionResult.Success)?.let {
                    _statistics.emit(result)
                }
                (this as? ActionResult.Error)?.let {

                }
            }
        }
    }

    fun calendarDayItemClicked(context: Context, name: String) {
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

    fun experienceClickedIndividual(
        context: Context,
        name: String,
        status: String,
        awareStartDt: String,
        orderId: Int
    ) {
        viewModelScope.launch {
            analytic.send(
                CalendarDateExperienceIndividual(
                    name,
                    context,
                    getGuideEmailUseCase.invoke().first() ?: "",
                    getGuideIdUseCase.invoke().first() ?: 0,
                    experienceType = OrderTypes.PRIVATE.type,
                    orderStatus = status.statusValueIndividual(awareStartDt, ""),
                    orderNumber = orderId,
                    menuButton = MenuItems.CALENDAR.type
                )
            )
        }
    }

    fun experienceClickedGroupTour(
        context: Context,
        name: String,
        status: String,
        awareStartDt: String,
        eventNumber: Int,
        type: String
    ) {
        viewModelScope.launch {
            analytic.send(
                CalendarDateExperienceGroupTour(
                    name,
                    context,
                    getGuideEmailUseCase.invoke().first() ?: "",
                    getGuideIdUseCase.invoke().first() ?: 0,
                    experienceType = type.tourType(),
                    eventStatus = status.statusValueGroupTour(awareStartDt),
                    eventNumber = eventNumber,
                    menuButton = MenuItems.CALENDAR.type
                )
            )
        }
    }

    fun menuItemClicked(context: Context, menuItem: String) {
        viewModelScope.launch {
            analytic.send(
                MenuItemClicked(
                    name = AnalyticsConstants.CALENDAR_DAY_MENU_BUTTON,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    menuButton = menuItem
                )
            )
        }
    }
}