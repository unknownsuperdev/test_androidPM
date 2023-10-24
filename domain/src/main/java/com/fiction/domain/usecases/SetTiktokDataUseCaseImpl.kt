package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.SetTiktokDataUseCase
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.domain.utils.Constants
import org.json.JSONObject

class SetTiktokDataUseCaseImpl(
    private val analyticRepo: AnalyticRepo,
    private val dispatcher: CoroutineDispatcherProvider,
) : SetTiktokDataUseCase {

    override suspend fun invoke(analyticTiktok: JSONObject): ActionResult<String> =
        with(dispatcher.io) {
            val tikTokJson = JSONObject()
            tikTokJson.put("webinfo", analyticTiktok)
            when (val apiData = analyticRepo.setTiktokData(tikTokJson)) {
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