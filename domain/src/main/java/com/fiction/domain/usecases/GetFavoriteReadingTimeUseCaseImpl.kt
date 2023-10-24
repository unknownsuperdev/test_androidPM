package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetFavoriteReadingTimeUseCase
import com.fiction.domain.model.onboarding.FavoriteReadingTime
import com.fiction.domain.repository.OnBoardingRepo
import kotlinx.coroutines.withContext

class GetFavoriteReadingTimeUseCaseImpl(
    private val onBoardingRepo: OnBoardingRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetFavoriteReadingTimeUseCase {

    override suspend fun invoke(): ActionResult<List<FavoriteReadingTime>> =
        withContext(dispatcher.io) {
            when (val apiData = onBoardingRepo.getOnBoardingSetting()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        val readingTimes =
                            response.readingTime?.map { FavoriteReadingTime.from(it) } ?: listOf()
                        ActionResult.Success(readingTimes)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
