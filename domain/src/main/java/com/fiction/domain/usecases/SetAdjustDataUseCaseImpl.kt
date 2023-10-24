package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.analytic.AnalyticAdjust
import com.fiction.domain.interactors.SetAdjustDataUseCase
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.domain.utils.Constants

class SetAdjustDataUseCaseImpl(
    private val analyticRepo: AnalyticRepo,
    private val dispatcher: CoroutineDispatcherProvider,
) : SetAdjustDataUseCase {

    override suspend fun invoke(analyticAdjust: AnalyticAdjust): ActionResult<String> =
        with(dispatcher.io) {
        val analyticAdjustRequest = AnalyticAdjust.to(analyticAdjust)
        when (val apiData = analyticRepo.setAdjustData(analyticAdjustRequest)) {
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