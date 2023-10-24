package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CompleteGiftUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.repository.GiftRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class CompleteGiftUseCaseImpl(
    private val giftRepository: GiftRepository,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : CompleteGiftUseCase {

    override suspend fun invoke(eventIds: List<Int>, giftCount: Int): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = giftRepository.completeGift(eventIds)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        getProfileInfoUseCase.updateBalance(
                            giftCount
                        )
                        ActionResult.Success(response.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}