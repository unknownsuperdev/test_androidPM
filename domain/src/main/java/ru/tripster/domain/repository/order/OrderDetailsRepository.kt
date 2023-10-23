package ru.tripster.domain.repository.order

import ru.tripster.core.ActionResult
import ru.tripster.entities.request.cancel.OrderCancelRequest
import ru.tripster.entities.request.chat.TravelerContactsOpenRequest
import ru.tripster.entities.response.orders.OrderCounterResponse
import ru.tripster.entities.response.orders.OrderDetailsResponse
import ru.tripster.entities.response.orders.ordersreject.OrderRejectResponse


interface OrderDetailsRepository {
    suspend fun getOrders(id: Int): ActionResult<OrderDetailsResponse>
    suspend fun cancelOrder(id: Int, cancelOrder: OrderCancelRequest): ActionResult<Unit>
    suspend fun getRejectInfo(id: Int): ActionResult<OrderRejectResponse>
    suspend fun getCounters(): ActionResult<OrderCounterResponse>
    suspend fun editOrder(
        id: Int,
        travelerContactsOpen: TravelerContactsOpenRequest
    ): ActionResult<OrderDetailsResponse>
}