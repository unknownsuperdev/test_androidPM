package ru.tripster.guide.ui.fragments.orderDetails.individual

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.events.GetCurrentGteEventUseCase
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.order.GetMenuItemUseCase
import ru.tripster.domain.interactors.order.OrderDetailsUseCase
import ru.tripster.domain.model.events.EventResults
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.UserTapExperienceIndividual
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.enums.OrderTypes
import ru.tripster.guide.extensions.statusValueIndividual

class OrderDetailsIndividualViewModel(
    private val orderDetailsUseCase: OrderDetailsUseCase,
    private val setCurrentGteEventUseCase: GetCurrentGteEventUseCase,
    private val analytic: Analytic,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getGuideIdUseCase: GetGuideIdUseCase,
    private val getMenuItemUseCase: GetMenuItemUseCase
) :
    BaseViewModel() {

    private val _orderDetails: MutableStateFlow<OrderDetails?> by lazy { MutableStateFlow(null) }
    val orderDetails = _orderDetails.asStateFlow()

    private val _orderDetailsError = MutableSharedFlow<String?>()
    val orderDetailsError = _orderDetailsError.asSharedFlow()

    private val _orderCurrentEvent: MutableStateFlow<EventResults?> by lazy { MutableStateFlow(null) }
    val orderCurrentEvent = _orderCurrentEvent.asStateFlow()

    var currentProgress = 0.0f
    var result = ""
    var comment = ""
    var orderDetailsValue: OrderDetails? = null
    var booleanOfContacts = false
    var isNavigated = false
    var showConfirmButton = false
    var resultOfBottomSheet = false
    var lastModifiedDate = ""
    var isModified = false
    var meetingPointViewMoreClicked = true
    var titleHeight = 0
    var messageContentWidth = 0
    var meetingPointWidth = 0

    fun getOrderDetails(orderId: Int) {
        viewModelScope.launch {
            when (val result = orderDetailsUseCase(orderId)) {
                is ActionResult.Success -> {
                    _orderDetails.emit(result.result)
                }
                is ActionResult.Error -> {

                }
            }
        }
    }

    fun setOrderCurrentEvent(orderId: Int, guideLastVisitDate: String) {
        viewModelScope.launch {
           if (_orderCurrentEvent.value == null) when (val result = setCurrentGteEventUseCase(orderId, guideLastVisitDate)) {
                is ActionResult.Success -> {
                    _orderCurrentEvent.emit(result.result)
                }
                is ActionResult.Error -> {
                    _orderDetailsError.emit(result.errors.message)
                }
            }
        }
    }

    fun experienceClickedIndividual(
        context: Context,
        name: String,
        menuItem: String? = null
    ) {
        viewModelScope.launch {
            analytic.send(
                UserTapExperienceIndividual(
                    name = name,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    experienceType = OrderTypes.PRIVATE.type,
                    orderStatus = _orderDetails.value?.status?.statusValueIndividual(
                        _orderDetails.value?.event?.awareStartDt ?: "",
                        _orderDetails.value?.reject?.initiator ?: ""
                    ) ?: "",
                    orderNumber = _orderDetails.value?.id ?: 0,
                    menuButton = menuItem ?: getMenuItemUseCase.invoke().first() ?: "",
                    touristId = _orderDetails.value?.traveler?.id ?: 0
                )
            )

        }

    }
}