package com.fiction.data.dataservice.apiservice

import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.gifts.CompleteGiftResponse
import com.fiction.entities.response.gifts.EventIdsRequest
import com.fiction.entities.response.gifts.GiftExistResponse
import com.fiction.entities.response.gifts.WelcomeGiftResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface GiftsService {

    @GET("v1/events/exist")
    suspend fun getIsExistGift(): Response<BaseResultModel<GiftExistResponse>>

    @GET("v1/events")
    suspend fun getGift(): Response<BaseResultModel<List<WelcomeGiftResponse>>>

    @POST("v1/events/complete")
    suspend fun completeGift(
        @Body eventIds: EventIdsRequest
    ): Response<BaseResultModel<CompleteGiftResponse>>
}