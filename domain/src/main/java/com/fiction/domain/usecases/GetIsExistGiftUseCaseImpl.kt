package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetIsExistGiftUseCase
import com.fiction.domain.repository.GiftRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetIsExistGiftUseCaseImpl(
    private val giftRepository: GiftRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GetIsExistGiftUseCase {

    override suspend fun invoke(): ActionResult<Boolean> =
        withContext(dispatcher.io) {
            when (val apiData = giftRepository.getIsExistGift()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        ActionResult.Success(response.exist)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}