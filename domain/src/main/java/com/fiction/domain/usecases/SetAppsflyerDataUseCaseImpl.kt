package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.SetAppsflyerDataUseCase
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.domain.utils.Constants
import com.fiction.entities.request.analytic.AnalyticAppsflyerRequest

class SetAppsflyerDataUseCaseImpl(
    private val analyticRepo: AnalyticRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : SetAppsflyerDataUseCase {

    override suspend fun invoke(appsflyerId: String?, idfa: String?, uniqueId: String?): ActionResult<String> =
        with(dispatcher.io) {
            val analyticAppsflyerRequest = AnalyticAppsflyerRequest(appsflyerId, idfa, uniqueId)
            when (val apiData = analyticRepo.setAppsflyerData(analyticAppsflyerRequest)) {
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