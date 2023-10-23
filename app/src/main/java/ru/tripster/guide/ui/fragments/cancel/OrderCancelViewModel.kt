package ru.tripster.guide.ui.fragments.cancel

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
import ru.tripster.domain.interactors.order.CancelOrderUseCase
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.interactors.order.GetRejectInfoUseCase
import ru.tripster.domain.model.cancel.OrderCancelData
import ru.tripster.domain.model.order.orderrejectinfo.RejectInfo
import ru.tripster.domain.model.statistics.OrderStatisticsData
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.UserTapCancelButton
import ru.tripster.guide.appbase.viewmodel.BaseViewModel
import ru.tripster.guide.enums.RejectedOrderTypes
import ru.tripster.guide.extensions.statusValueIndividual
import ru.tripster.guide.extensions.tourType
import ru.tripster.guide.ui.fragments.cancel.model.OrderCancelReasonData
import ru.tripster.guide.ui.fragments.cancel.model.SubDataType

class OrderCancelViewModel(
    private val getRejectInfoUseCase: GetRejectInfoUseCase,
    private val cancelOrderUseCase: CancelOrderUseCase,
    private val analytic: Analytic,
    private val getGuideEmailUseCase: GetGuideEmailUseCase,
    private val getGuideIdUseCase: GetGuideIdUseCase
) : BaseViewModel() {
    private val _orderRejectInfo: MutableStateFlow<RejectInfo?> by lazy { MutableStateFlow(null) }
    val orderRejectInfo = _orderRejectInfo.asSharedFlow()

    private val _cancelSuccess: MutableSharedFlow<Boolean> by lazy { MutableSharedFlow() }
    val cancelSuccess = _cancelSuccess.asSharedFlow()

    private val _cancelError: MutableSharedFlow<String> by lazy { MutableSharedFlow() }
    val cancelError = _cancelError.asSharedFlow()

    private val _errorGetReject: MutableSharedFlow<Boolean?> by lazy { MutableSharedFlow() }
    val errorGetReject = _errorGetReject.asSharedFlow()

    var cancelReasonComment: String? = null
    var selectedDate: String = ""
    var confirmationRateValue: Int = 0
    var statisticsValue: OrderStatisticsData? = null
    var cancelOrderData: OrderCancelData? = null
    var orderRejectValue: RejectInfo? = null
    var checkedRadioId = 0

    fun cancelOrder(id: Int, cancelOrderData: OrderCancelData) {
        viewModelScope.launch {
            cancelOrderUseCase(id, cancelOrderData).run {
                (this as? ActionResult.Success)?.let {
                    _cancelSuccess.emit(result)
                }
                (this as? ActionResult.Error)?.let {
                    val errorBody = (errors as? CallException)?.errorMessage
                    _cancelError.emit(errorBody ?: errors.message ?: "")
                }
            }
        }
    }

    fun getOrderRejectInfo(id: Int) {
        viewModelScope.launch {
            getRejectInfoUseCase(id).run {
                (this as? ActionResult.Success)?.let {
                    _orderRejectInfo.emit(result)
                }
                (this as? ActionResult.Error)?.let {
                    _errorGetReject.emit(true)
                }
            }
        }
    }

    fun setViewContent(): List<OrderCancelReasonData> {
        return listOf(
            OrderCancelReasonData(
                0,
                "Не могу провести\nЛичные дела или другой заказ",
                null
            ), OrderCancelReasonData(
                1,
                "Путешественник отказался\nПередумал, не подошли условия",
                null
            ), OrderCancelReasonData(
                2,
                "Путешественник не отвечает\nЖду ответа более 24 часов",
                null
            ), OrderCancelReasonData(
                3,
                "Заказ на занятый день\nПросят закрытый день. Отмена не повлияет на статистику",
                SubDataType.DATE_TYPE,
            ), OrderCancelReasonData(
                4,
                "Дубликат другого заказа\nСоздан лишний заказ. Отмена не повлияет на статистику",
                SubDataType.RADIO_TYPE,
            ), OrderCancelReasonData(
                5,
                "Заказ не соответсвует условиям\nПросят не то, что я предлагаю.\nОтмена не повлияет на статистику",
                SubDataType.MESSAGE_TYPE,
            )
        )
    }

    fun cancelButtonClicked(
        context: Context,
        orderId: Int,
        touristId: Int,
        status: String,
        type:String,
        awareStartDt: String
    ) {
        viewModelScope.launch {
            analytic.send(
                UserTapCancelButton(
                    context,
                    email = getGuideEmailUseCase.invoke().first() ?: "",
                    guidId = getGuideIdUseCase.invoke().first() ?: 0,
                    experienceType = type.tourType(),
                    orderNumber = orderId,
                    orderStatus = status.statusValueIndividual(awareStartDt,""),
                    touristId = touristId,
                    orderRejectReason = when (checkedRadioId - 11) {
                        0 -> RejectedOrderTypes.CAN_NOT_TAKE.type
                        1 -> RejectedOrderTypes.TRAVELLER_REFUSED.type
                        2 -> RejectedOrderTypes.TRAVELLER_NOT_RESPOND.type
                        3 -> RejectedOrderTypes.BUSY_DAY.type
                        4 -> RejectedOrderTypes.DUPLICATED_ORDER.type
                        else -> RejectedOrderTypes.NOT_ABOUT_EXPERIENCE.type
                    }
                )
            )
        }
    }
}