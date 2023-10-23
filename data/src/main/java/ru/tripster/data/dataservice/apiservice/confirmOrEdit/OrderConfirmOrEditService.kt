package ru.tripster.data.dataservice.apiservice.confirmOrEdit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query
import ru.tripster.entities.request.confirm.OrderConfirmRequest
import ru.tripster.entities.request.confirm.TicketRequest
import ru.tripster.entities.response.experience.ChangeExperienceResponse
import ru.tripster.entities.response.orders.OrderDetailsResponse

interface OrderConfirmOrEditService {

    @POST("guides/v2/orders/{id}/confirm/")
    suspend fun confirmOrder(
        @Path("id") id: Int,
        @Body orderConfirmRequest: OrderConfirmRequest
    ): Response<OrderDetailsResponse>

    @PATCH("guides/v2/orders/{id}/")
    suspend fun editOrders(
        @Path("id") id: Int,
        @Body orderConfirmRequest: OrderConfirmRequest
    ): Response<OrderDetailsResponse>

    @GET("web/v1/experiences/{experienceId}/price/")
    suspend fun changeExperience(
        @Path("experienceId") experienceId: Int,
        @Query("persons_count") personsCount: Int,
        @Query("date") date: String,
        @Query("tickets") tickets: String?,
        @Query("custom_price") customPrice: Double?,
        @Query("time") time: String?
    ): Response<ChangeExperienceResponse>

}