package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetGiftUseCase
import com.fiction.domain.model.gift.WelcomeGift
import com.fiction.domain.repository.GiftRepository
import com.fiction.domain.utils.Constants
import com.fiction.entities.response.gifts.GiftBodyResponse
import com.google.gson.Gson
import kotlinx.coroutines.withContext

class GetGiftUseCaseImpl(
    private val giftRepository: GiftRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GetGiftUseCase {

    override suspend fun invoke(): ActionResult<WelcomeGift> =
        withContext(dispatcher.io) {
            when (val apiData = giftRepository.getGift()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        if (response.isNotEmpty()) {
                            val item =
                                Gson().fromJson(response[0].body, GiftBodyResponse::class.java)
                            val gift = WelcomeGift.from(response[0], item.content ?: "", item.coins ?: 0)
                            ActionResult.Success(gift)
                        } else ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}