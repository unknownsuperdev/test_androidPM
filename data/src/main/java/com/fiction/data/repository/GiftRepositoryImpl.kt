package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.GiftsService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.GiftRepository
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.gifts.CompleteGiftResponse
import com.fiction.entities.response.gifts.EventIdsRequest
import com.fiction.entities.response.gifts.GiftExistResponse
import com.fiction.entities.response.gifts.WelcomeGiftResponse

class GiftRepositoryImpl(
    private val giftsService: GiftsService
) : GiftRepository {
    override suspend fun getIsExistGift(): ActionResult<BaseResultModel<GiftExistResponse>> =
        makeApiCall {
            analyzeResponse(giftsService.getIsExistGift())
        }

    override suspend fun getGift(): ActionResult<BaseResultModel<List<WelcomeGiftResponse>>> =
        makeApiCall {
            analyzeResponse(giftsService.getGift())
        }

    override suspend fun completeGift(eventIds: List<Int>): ActionResult<BaseResultModel<CompleteGiftResponse>> =
        makeApiCall {
            analyzeResponse(giftsService.completeGift(EventIdsRequest(eventIds)))
        }
}