package ru.tripster.data.repository.order

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.order.OrdersApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.order.OrderDetailsRepository
import ru.tripster.entities.request.cancel.OrderCancelRequest
import ru.tripster.entities.request.chat.TravelerContactsOpenRequest
import ru.tripster.entities.response.orders.OrderCounterResponse
import ru.tripster.entities.response.orders.OrderDetailsResponse
import ru.tripster.entities.response.orders.ordersreject.OrderRejectResponse

class OrdersRepositoryImpl(private val ordersApiService: OrdersApiService) :
    OrderDetailsRepository {
    override suspend fun getOrders(id: Int): ActionResult<OrderDetailsResponse> = makeApiCall {
        analyzeResponse(ordersApiService.getOrders(id))
    }

    override suspend fun cancelOrder(id: Int, cancelOrder: OrderCancelRequest): ActionResult<Unit> =
        makeApiCall {
            analyzeResponse(ordersApiService.cancelOrder(id, cancelOrder))
        }

    override suspend fun getRejectInfo(id: Int): ActionResult<OrderRejectResponse> = makeApiCall {
        analyzeResponse(ordersApiService.getRejectInfo(id))
    }

    override suspend fun getCounters(): ActionResult<OrderCounterResponse> = makeApiCall {
        analyzeResponse(ordersApiService.getOrderCounter())
    }

    override suspend fun editOrder(
        id: Int,
        travelerContactsOpen: TravelerContactsOpenRequest
    ): ActionResult<OrderDetailsResponse> = makeApiCall {
        analyzeResponse(ordersApiService.editOrders(id, travelerContactsOpen))
    }
}