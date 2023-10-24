package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.gifts.CompleteGiftResponse
import com.fiction.entities.response.gifts.GiftExistResponse
import com.fiction.entities.response.gifts.WelcomeGiftResponse

interface GiftRepository {
    suspend fun getIsExistGift(): ActionResult<BaseResultModel<GiftExistResponse>>
    suspend fun getGift(): ActionResult<BaseResultModel<List<WelcomeGiftResponse>>>
    suspend fun completeGift(eventIds: List<Int>): ActionResult<BaseResultModel<CompleteGiftResponse>>
}