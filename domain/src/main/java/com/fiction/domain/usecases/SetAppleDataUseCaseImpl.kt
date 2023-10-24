package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.analytic.AnalyticApple
import com.fiction.domain.interactors.SetAppleDataUseCase
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.domain.utils.Constants

class SetAppleDataUseCaseImpl(
    private val analyticRepo: AnalyticRepo,
    private val dispatcher: CoroutineDispatcherProvider,
) : SetAppleDataUseCase {

    override suspend fun invoke(analyticApple: AnalyticApple): ActionResult<String> =
        with(dispatcher.io) {
            val analyticAppleRequest = AnalyticApple.to(analyticApple)
            when (val apiData = analyticRepo.setAppleData(analyticAppleRequest)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        ActionResult.Success(it.status.toString())
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}