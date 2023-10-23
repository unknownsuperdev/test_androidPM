package ru.tripster.guide.ui.fragments.orderDetails.groupTour

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.events.GetCurrentGteEventUseCase
import ru.tripster.domain.interactors.events.SetCurrentAvailablePlacesUseCase
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.order.GetMenuItemUseCase
import ru.tripster.domain.interactors.order.OrderDetailsUseCase
import ru.tripster.domain.model.events.Order
import ru.tripster.domain.model.events.OrderDetailsGroup
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.guide.analytics.*
import ru.tripster.guide.analytics.MenuItemClicked
import ru.tripster.guide.analytics.UserTapExperienceGroupTour
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.extensions.dateFormattingWithHour
import ru.tripster.guide.extensions.statusValueGroupTour
import ru.tripster.guide.extensions.tourType

class OrderDetailsGroupTourViewModel(
    private val getCurrentGteEventUseCase: GetCurrentGteEventUseCase,
    private val setCurrentAvailablePlacesUseCase: SetCurrentAvailablePlacesUseCase,
    private val orderDetailsUseCase: OrderDetailsUseCase,
    private val analytic: Analytic,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getGuideIdUseCase: GetGuideIdUseCase,
    private val getMenuItemUseCase: GetMenuItemUseCase
) : BaseViewModel() {
    private val _orderDetails = MutableStateFlow<OrderDetailsGroup?>(null)
    val orderDetails = _orderDetails

    private val _orderDetailsError = MutableSharedFlow<String?>()
    val orderDetailsError = _orderDetailsError.asSharedFlow()

    private val _perOrderDetails: MutableStateFlow<OrderDetails?> by lazy { MutableStateFlow(null) }
    val perOrderDetails = _perOrderDetails.asSharedFlow()

    var currentProgress = 0.0f
    var isFromCloseBooking: Boolean? = null
    var orderDetailsGroup: OrderDetailsGroup? = null
    var perOrderValue: OrderDetails? = null
    var orderList = listOf<Order>()
    private val travelers = mutableListOf<String>()
    var isNavigated = false
    var titleHeight = 0
    fun setNewOrderDetails(groupOrderId: Int, guideLastVisitDate: String) {
        viewModelScope.launch {
            when (val result = getCurrentGteEventUseCase(groupOrderId, guideLastVisitDate)) {
                is ActionResult.Success -> {
                    _orderDetails.emit(result.result.run {
                        OrderDetailsGroup(
                            status = status,
                            title = experience.title,
                            orders = orders,
                            maxPersons = maxPersons,
                            numberOfPersonsAvail = numberOfPersonsAvail,
                            date = (date + 'T' + time).dateFormattingWithHour(
                                experience.duration
                            ),
                            currency = experience.price.currency,
                            type = experience.type,
                            duration = experience.duration,
                            awareStartDt = awareStartDt,
                            numberOfPersonsPaid = numberOfPersonsPaid,
                            coverImage = experience.coverImage
                        )
                    })
                }
                is ActionResult.Error -> {
                    _orderDetailsError.emit(result.errors.message)
                }
            }
        }
    }


    fun getOrderDetails(id: Int) {
        viewModelScope.launch {
            when (val result = orderDetailsUseCase.invoke(id)) {
                is ActionResult.Success -> {
                    _perOrderDetails.emit(result.result)
                }
                is ActionResult.Error -> {

                }
            }
        }
    }

    fun setNewAvailableSpaceAmount(
        groupOrderId: Int,
        availablePlaces: Int,
        lastVisitDate: String
    ) {
        viewModelScope.launch {
            when (val result =
                setCurrentAvailablePlacesUseCase(groupOrderId, availablePlaces, lastVisitDate)) {
                is ActionResult.Success -> {
                    _orderDetails.emit(result.result.run {
                        OrderDetailsGroup(
                            status = status,
                            title = experience.title,
                            orders = orders,
                            maxPersons = maxPersons,
                            numberOfPersonsAvail = numberOfPersonsAvail,
                            date = (date + 'T' + time).dateFormattingWithHour(
                                experience.duration
                            ),
                            currency = experience.price.currency,
                            type = experience.type,
                            duration = experience.duration,
                            awareStartDt = awareStartDt,
                            numberOfPersonsPaid = numberOfPersonsPaid,
                            coverImage = experience.coverImage
                        )
                    })
                }
                is ActionResult.Error -> {

                }
            }
        }
    }

    fun orderDetailsGroupTourClicked(
        context: Context,
        name: String,
        eventId: Int,
        orderId: Int? = null,
        individualId: Int = 0
    ) {
        var touristId = 0
        travelers.clear()
        if (individualId != 0) touristId = individualId else {
            if (orderList.size > 1)
                orderList.forEach {
                    travelers.add(it.traveler.id.toString())
                } else touristId = orderList[0].traveler.id
        }

        viewModelScope.launch {
            if (orderId == null)
                analytic.send(
                    UserTapExperienceGroupTour(
                        name,
                        context,
                        getGuideEmailUseCase.invoke().first() ?: "",
                        getGuideIdUseCase.invoke().first() ?: 0,
                        experienceType = _orderDetails.value?.type?.tourType() ?: "",
                        eventNumber = eventId,
                        eventStatus = _orderDetails.value?.status?.statusValueGroupTour(
                            _orderDetails.value?.awareStartDt ?: ""
                        ) ?: "",
                        menuButton = getMenuItemUseCase.invoke().first() ?: "",
                        touristIds = if (touristId != 0) touristId.toString() else
                            Gson().toJson(
                                travelers
                            )
                    )
                ) else analytic.send(
                UserTapExperienceGroupTourWithOrderNumber(
                    name,
                    context,
                    getGuideEmailUseCase.invoke().first() ?: "",
                    getGuideIdUseCase.invoke().first() ?: 0,
                    experienceType = _orderDetails.value?.type?.tourType() ?: "",
                    eventNumber = eventId,
                    orderNumber = orderId,
                    eventStatus = _orderDetails.value?.status?.statusValueGroupTour(
                        _orderDetails.value?.awareStartDt ?: ""
                    ) ?: "",
                    menuButton = getMenuItemUseCase.invoke().first() ?: "",
                    touristIds = if (touristId != 0) touristId.toString() else
                        Gson().toJson(
                            travelers
                        )
                )
            )
        }
    }

    fun menuItemClicked(context: Context, menuItem: String) {
        viewModelScope.launch {
            analytic.send(
                MenuItemClicked(
                    name = AnalyticsConstants.GROUP_EXP_MENU_BUTTON,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    menuButton = menuItem
                )
            )
        }
    }
}