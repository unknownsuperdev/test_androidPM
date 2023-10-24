package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetOnBoardingScreensUseCase
import com.fiction.domain.repository.OnBoardingRepo
import kotlinx.coroutines.withContext

class GetOnBoardingScreensUseCaseImpl(
    private val onBoardingRepo: OnBoardingRepo,
    private val dispatcher: CoroutineDispatcherProvider
): GetOnBoardingScreensUseCase {

    override suspend fun invoke(): ActionResult<List<String>>  =
        withContext(dispatcher.io) {
            when (val apiData = onBoardingRepo.getOnBoardingSetting()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        val genders = response.screens ?: listOf()
                        ActionResult.Success(genders)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
