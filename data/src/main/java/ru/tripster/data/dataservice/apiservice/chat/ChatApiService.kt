package ru.tripster.data.dataservice.apiservice.chat

import retrofit2.Response
import retrofit2.http.*
import ru.tripster.entities.request.chat.OrderCommentRequest
import ru.tripster.entities.request.chat.OrderCommentViewedRequest
import ru.tripster.entities.response.chat.OrderCommentsResponse
import ru.tripster.entities.response.chat.ResultResponse

interface ChatApiService {
    @GET("guides/v2/order-comments/")
    suspend fun getOrderComments(
        @Query("order") orderId: Int,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String? = null,
    ): Response<OrderCommentsResponse>

    @POST("guides/v2/order-comments/")
    suspend fun postOrderComment(
        @Body comment: OrderCommentRequest
    ): Response<ResultResponse>

    @PATCH("guides/v2/order-comments/{id}/")
    suspend fun chatIsOpened(
        @Path("id") commentId: Int,
        @Body comment: OrderCommentViewedRequest
    ): Response<ResultResponse>
}