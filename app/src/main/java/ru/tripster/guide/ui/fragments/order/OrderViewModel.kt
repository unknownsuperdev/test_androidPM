package ru.tripster.guide.ui.fragments.order

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.events.EventCountersUseCase
import ru.tripster.domain.interactors.order.OrderDetailsUseCase
import ru.tripster.domain.interactors.profile.GetUserProfileInfoUseCase
import ru.tripster.domain.interactors.statistics.StatisticsUseCase
import ru.tripster.domain.model.events.EventCounters
import ru.tripster.domain.model.events.EventsCallSortingModel
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.domain.model.profile.UserInfo
import ru.tripster.domain.model.statistics.OrderStatisticsData
import ru.tripster.domain.usecases.events.EventsUseCase
import ru.tripster.guide.analytics.*
import ru.tripster.guide.analytics.UserTapChips
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.domain.model.MenuItems
import ru.tripster.guide.analytics.AnalyticsConstants.ORDERS_MENU_BUTTON
import ru.tripster.guide.enums.OrderTypes
import ru.tripster.guide.extensions.statusValueGroupTour
import ru.tripster.guide.extensions.statusValueIndividual
import ru.tripster.guide.extensions.tourType

class OrderViewModel(
    private val eventsUseCase: EventsUseCase,
    private val eventCountersUseCase: EventCountersUseCase,
    private val statistics: StatisticsUseCase,
    private val orderDetailsUseCase: OrderDetailsUseCase,
    private val analytics: Analytic,
    private val profileInfoUseCase: GetUserProfileInfoUseCase
) : BaseViewModel() {

    private val _eventCounters = MutableStateFlow<EventCounters?>(null)
    val eventCounters = _eventCounters.asStateFlow()

    private val _statistics: MutableSharedFlow<OrderStatisticsData> by lazy { MutableSharedFlow() }
    val statisticsData = _statistics.asSharedFlow()

    private val _errorDataEventCounters = MutableStateFlow<String?>(null)

    private val filterFlow = MutableSharedFlow<EventsCallSortingModel>()

    private val _profileInfo: MutableSharedFlow<UserInfo> by lazy { MutableSharedFlow() }
    val profileInfo = _profileInfo.asSharedFlow()

    val chipId = MutableStateFlow(0)

    private val _orderDetails: MutableStateFlow<OrderDetails?> by lazy { MutableStateFlow(null) }
    val orderDetails = _orderDetails.asStateFlow()
    private val _errorOrderDetails = MutableStateFlow<String?>(null)

    val errorOrderDetails = _errorOrderDetails.asSharedFlow()

    var totalCount: Int? = null
    var backPressed = false
    var secondEnter = false
    var eventBack = false
    var isModified = false
    var isScrollable = false
    var isReturned = false
    var currentChipData = ""
    var chipData = ""
    var isReadyToScrollCount = 0
    var chipName = "Все в работе"
    var guidEmail = ""
    var guidId = 0
    var isLoaded = false
    var delayLayer = false
    var currentProgress = 0
    var totalScrollRange = 0

    init {
        viewModelScope.launch {
            getStatistics()

            when (val userInfo = callData(profileInfoUseCase())) {
                is ActionResult.Success -> {
                    _profileInfo.emit(userInfo.result)
                }

                is ActionResult.Error -> {}
            }
        }
    }


     val pagingData = filterFlow
        .map { eventsUseCase.execute(it) }
        .flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty()).cachedIn(viewModelScope)

    fun changeQuery(data: EventsCallSortingModel) {
        viewModelScope.launch {
            filterFlow.emit(data)
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

    fun getEventCounters() {
        viewModelScope.launch {
            when (val result = eventCountersUseCase.invoke()) {
                is ActionResult.Success -> {
                    _eventCounters.emit(result.result)
                    _eventCounters.value?.apply {
                        totalCount =
                            booked + canceled + confirmation + finished + inWork + pendingPayment + unread
                    }
                }
                is ActionResult.Error -> {
                    _errorDataEventCounters.value = result.errors.message
                }
            }
        }
    }

    fun getOrderDetails(id: Int) {
        viewModelScope.launch {
            when (val result = orderDetailsUseCase.invoke(id)) {
                is ActionResult.Success -> {
                    _orderDetails.emit(result.result)
                }
                is ActionResult.Error -> {
                    _errorOrderDetails.emit("Order not found")
                }
            }
        }
    }

    fun chipClick(context: Context) {
        analytics.send(UserTapChips(context, guidEmail, guidId, chipName))
    }

    fun experienceClickedIndividual(
        context: Context,
        name: String,
        status: String,
        awareStartDt: String,
        initiator: String,
        orderId: Int
    ) {
        analytics.send(
            UserTapExperienceIndividualOrder(
                name,
                context,
                guidEmail,
                guidId,
                chipName,
                experienceType = OrderTypes.PRIVATE.type,
                orderStatus = status.statusValueIndividual(awareStartDt, initiator),
                orderNumber = orderId,
                menuButton = MenuItems.ORDERS.type
            )
        )
    }


    fun experienceClickedGroupTour(
        context: Context,
        name: String,
        status: String,
        awareStartDt: String,
        eventNumber: Int,
        type: String
    ) {
        analytics.send(
            UserTapExperienceGroupTourOrder(
                name,
                context,
                guidEmail,
                guidId,
                chipName,
                experienceType = type.tourType(),
                eventStatus = status.statusValueGroupTour(awareStartDt),
                eventNumber = eventNumber,
                menuButton = MenuItems.ORDERS.type
            )
        )
    }

    fun menuItemClicked(context: Context, menuItem:String) {
        analytics.send(
            MenuItemClicked(
                name = ORDERS_MENU_BUTTON,
                context = context,
                email = guidEmail,
                guidId = guidId,
                menuButton = menuItem
            )
        )
    }

}