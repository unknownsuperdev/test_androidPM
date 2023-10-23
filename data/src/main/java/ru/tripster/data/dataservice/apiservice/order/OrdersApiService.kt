package ru.tripster.data.dataservice.apiservice.order

import retrofit2.Response
import retrofit2.http.*
import ru.tripster.entities.request.cancel.OrderCancelRequest
import ru.tripster.entities.request.chat.TravelerContactsOpenRequest
import ru.tripster.entities.response.orders.OrderCounterResponse
import ru.tripster.entities.response.orders.OrderDetailsResponse
import ru.tripster.entities.response.orders.ordersreject.OrderRejectResponse


interface OrdersApiService {
    @GET("guides/v2/orders/{id}")
    suspend fun getOrders(@Path("id") id: Int): Response<OrderDetailsResponse>

    @GET("guides/v2/orders/{id}/reject_info/")
    suspend fun getRejectInfo(@Path("id") id: Int): Response<OrderRejectResponse>

    @GET("guides/v2/orders/counters/")
    suspend fun getOrderCounter(): Response<OrderCounterResponse>

    @PATCH("guides/v2/orders/{id}/")
    suspend fun editOrders(
        @Path("id") id: Int,
        @Body travelerContactsOpen: TravelerContactsOpenRequest
    ): Response<OrderDetailsResponse>

    @HTTP(method = "DELETE", path = "/api/guides/v2/orders/{id}/", hasBody = true)
    suspend fun cancelOrder(
        @Path("id") id: Int,
        @Body orderCancelRequest: OrderCancelRequest
    ): Response<Unit>
}