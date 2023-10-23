package ru.tripster.guide.ui.fragments.orderDetails.groupTour.userDetails

import android.content.Context
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.order.GetMenuItemUseCase
import ru.tripster.domain.interactors.order.OrderDetailsUseCase
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.AnalyticsConstants
import ru.tripster.guide.analytics.MenuItemClicked
import ru.tripster.guide.analytics.UserOrderGroupTourDetails
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.extensions.statusValueIndividual
import ru.tripster.guide.extensions.tourType

class UserOrderGroupTourDetailsViewModel(
    private val orderDetailsUseCase: OrderDetailsUseCase,
    private val analytic: Analytic,
    private val getGuideIdUseCase: GetGuideIdUseCase,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getMenuItemUseCase: GetMenuItemUseCase
) : BaseViewModel() {

    private val _orderDetails: MutableStateFlow<OrderDetails?> by lazy { MutableStateFlow(null) }
    val orderDetails = _orderDetails.asSharedFlow()

    private val _orderDetailsError = MutableSharedFlow<String?>()
    val orderDetailsError = _orderDetailsError.asSharedFlow()

    var details: OrderDetails? = null

    fun getOrderDetails(id: Int) {
        viewModelScope.launch {
            when (val result = orderDetailsUseCase.invoke(id)) {
                is ActionResult.Success -> {
                    _orderDetails.emit(result.result)
                }
                is ActionResult.Error -> {
                    _orderDetailsError.emit(result.errors.message)
                }
            }
        }
    }

    fun experienceClicked(context: Context, name: String, type: String) {
        viewModelScope.launch {
            analytic.send(
                UserOrderGroupTourDetails(
                    name = name,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    experienceType = type.tourType(),
                    orderStatus = _orderDetails.value?.status?.statusValueIndividual(
                        _orderDetails.value?.event?.awareStartDt ?: "",
                        _orderDetails.value?.reject?.initiator ?: ""
                    ) ?: "",
                    orderNumber = _orderDetails.value?.id ?: 0,
                    menuButton = getMenuItemUseCase.invoke().first() ?: "",
                    touristId = _orderDetails.value?.traveler?.id ?: 0
                )
            )
        }
    }

    fun menuItemClicked(context: Context, menuItem: String) {
        viewModelScope.launch {
            analytic.send(
                MenuItemClicked(
                    name = AnalyticsConstants.USER_TAP_MENU_BUTTON,
                    context = context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    menuButton = menuItem
                )
            )
        }
    }
}