package ru.tripster.guide.ui.fragments.confirmOrder

import android.content.Context
import android.util.Log
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import ru.tripster.core.ActionResult
import ru.tripster.core.CallException
import ru.tripster.domain.interactors.confirmOrEdit.*
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.order.OrderDetailsUseCase
import ru.tripster.domain.model.calendar.DateScheduleItem
import ru.tripster.domain.model.confirmOrEdit.OrderConfirmOrEditData
import ru.tripster.domain.model.order.*
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.AnalyticsConstants.USER_TAP_ORDER_CONFIRM_SUBMIT_BUTTON
import ru.tripster.guide.analytics.UserTapConfirmButton
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.extensions.currency
import ru.tripster.guide.extensions.currentDate
import ru.tripster.guide.extensions.statusValueIndividual
import ru.tripster.guide.extensions.tourType

class ConfirmOrEditOrderViewModel(
    private val orderDetailsUseCase: OrderDetailsUseCase,
    private val orderConfirmUseCase: OrderConfirmOrEditUseCase,
    private val changeItemInAdapterListUseCase: ChangeItemInAdapterListUseCase,
    private val ticketsListIdCountUseCase: TicketsListIdCountUseCase,
    private val getAdapterListModelUseCase: GetAdapterListModelUseCase,
    private val changeExperienceUseCase: ChangeExperienceUseCase,
    private val analytic: Analytic,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getGuideIdUseCase: GetGuideIdUseCase
) :
    BaseViewModel() {

    private val _selectedTickets: MutableStateFlow<List<PerTicketOrderDetail>?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val selectedTickets = _selectedTickets.asSharedFlow()

    private val _totalCount: MutableStateFlow<Int> by lazy { MutableStateFlow(0) }
    val totalCount = _totalCount.asSharedFlow()

    private val _orderDetails: MutableStateFlow<OrderDetails?> by lazy { MutableStateFlow(null) }
    val orderDetails = _orderDetails.asSharedFlow()

    private val _orderDetailsError: MutableSharedFlow<String?> by lazy { MutableSharedFlow() }
    val orderDetailsError = _orderDetailsError.asSharedFlow()

    private val _changedExperience: MutableSharedFlow<ChangeExperience?> by lazy {
        MutableSharedFlow()
    }
    val changedExperience = _changedExperience.asSharedFlow()

    private val _changedExperienceError = MutableSharedFlow<String?>()
    val changedExperienceError = _changedExperienceError.asSharedFlow()

    private val _confirmOrder: MutableStateFlow<OrderDetails?> by lazy { MutableStateFlow(null) }
    val confirmOrder = _confirmOrder.asSharedFlow()

    private val _confirmOrderError = MutableSharedFlow<String?>()
    val confirmOrderError = _confirmOrderError.asSharedFlow()

    var selectedTicketsList: List<PerTicketOrderDetail> = emptyList()
    var emailError = true
    var phoneError = true
    var phoneValidError = false
    var meetingPlaceError = true

    var totalPriceValue = 0
    var tourTime = ""
    var currency = ""
    var status = ""
    var autoSetDayBusy = false
    var hasCustomPrice = false
    var isPaid = false
    var pricingModel = ""
    var email = ""
    var phone = ""
    var meetingPlace = ""
    var selectedDate: String = ""
    var isAtLeastOne = false
    var keypadHeight = 0
    var isVisibleKeyBoard = false
    var priceValue = 0.0
    var totalMembersCount = 0
    var closedTimes: List<DateScheduleItem> = emptyList()

    private fun changeExperience(
        experienceId: Int, personsCount: Int,
        date: String,
        tickets: List<TicketType>?,
        customPrice: Double? = null,
        time: String? = null
    ) {
        viewModelScope.launch {

            when (val result =
                changeExperienceUseCase.invoke(
                    experienceId,
                    personsCount,
                    date,
                    tickets,
                    customPrice,
                    time
                )) {

                is ActionResult.Success -> {
                    _changedExperience.emit(result.result)

                    val (list, count) = getAdapterListModelUseCase.getAdapterListModel(
                        perTicket = result.result.perTicket,
                        ticketDefinitions = _orderDetails.value?.experience?.price?.perPerson,
                        maxPerson = _orderDetails.value?.event?.maxPersons ?: 0,
                        currency = currency.currency(),
                        discountRate = _orderDetails.value?.discountRate ?: 0.0,
                        hasCustomPrice = hasCustomPrice,
                        pricingModel = _orderDetails.value?.experience?.pricingModel ?: "",
                        status = status
                    )

                    _totalCount.emit(count)
                }

                is ActionResult.Error -> {
                    changeExperience(
                        experienceId,
                        personsCount,
                        date,
                        tickets,
                        customPrice,
                        time
                    )
                }
            }
        }
    }

    fun modifyTickets(
        perTicketOrderDetail: PerTicketOrderDetail?,
        isAdded: Boolean,
        customPrice: Double? = null,
        time: String? = null
    ) {
        viewModelScope.launch {
            val list = changeItemInAdapterListUseCase.changeItemInAdapterList(
                perTicketOrderDetail,
                _selectedTickets.value ?: emptyList(),
                hasCustomPrice,
                isAdded,
                _orderDetails.value?.event?.maxPersons ?: 0
            )

            val (tickets, count) = ticketsListIdCountUseCase.ticketsListIdCount(list)

            _totalCount.emit(count)
            _selectedTickets.emit(list)

            if ((!hasCustomPrice || customPrice != null) && _totalCount.value != 0) {
                changeExperience(
                    _orderDetails.value?.experience?.id ?: 0,
                    tickets = if (_orderDetails.value?.experience?.pricingModel == PricingModel.PER_TICKET.pricingModel) tickets else null,
                    date = selectedDate.ifEmpty { currentDate() },
                    personsCount = count,
                    customPrice = customPrice,
                    time = time
                )
            }
        }
    }

    fun getOrderDetails(id: Int) {
        viewModelScope.launch {
            when (val result = orderDetailsUseCase.invoke(id)) {
                is ActionResult.Success -> {
                    _orderDetails.emit(result.result)

                    val (list, count) = getAdapterListModelUseCase.getAdapterListModel(
                        perTicket = result.result.price.perPicket,
                        ticketDefinitions = result.result.experience.price.perPerson,
                        maxPerson = _orderDetails.value?.event?.maxPersons ?: 0,
                        currency = currency.currency(),
                        discountRate = _orderDetails.value?.discountRate ?: 0.0,
                        hasCustomPrice = _orderDetails.value?.hasCustomPrice ?: false,
                        pricingModel = _orderDetails.value?.experience?.pricingModel ?: "",
                        status = status
                    )

                    _totalCount.emit(count)

                    _selectedTickets.emit(
                        list
                    )
                }

                is ActionResult.Error -> {
                    _orderDetailsError.emit(result.errors.message)
                }
            }
        }
    }

    fun confirmOrder(
        id: Int,
        confirmOrderData: OrderConfirmOrEditData,
        isConfirm: Boolean
    ) {
        viewModelScope.launch {
            when (val result =
                orderConfirmUseCase.invoke(id, confirmOrderData, isConfirm)) {
                is ActionResult.Success -> {
                    phoneValidError = false
                    _confirmOrder.emit(result.result)
                }
                is ActionResult.Error -> {
                    _confirmOrderError.emit((result.errors as? CallException)?.errorMessage)
                }
            }
        }
    }

    fun confirmOrEditButtonClicked(
        name:String,
        context: Context,
        type: String
    ) {
        viewModelScope.launch {
            analytic.send(
                UserTapConfirmButton(
                    name,
                    context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    experienceType = type.tourType(),
                    orderStatus = _orderDetails.value?.status?.statusValueIndividual(
                        awareStartDt = _orderDetails.value?.event?.awareStartDt ?: "",
                        initiator = _orderDetails.value?.reject?.initiator ?: ""
                    ) ?: "",
                    orderNumber = _orderDetails.value?.id ?: 0,
                    touristId = _orderDetails.value?.traveler?.id ?: 0
                )
            )
        }
    }

}