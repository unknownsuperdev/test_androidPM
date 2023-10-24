package com.fiction.domain.usecases

import android.util.Log
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.core.utils.Uuid
import com.fiction.domain.interactors.SetGoogleDataUseCase
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.domain.utils.Constants
import org.json.JSONObject

class SetGoogleDataUseCaseImpl(
    private val analyticRepo: AnalyticRepo,
    private val dispatcher: CoroutineDispatcherProvider,
    private val uuid: Uuid
) : SetGoogleDataUseCase {

    override suspend fun invoke(analyticGoogle: JSONObject): ActionResult<String> =
        with(dispatcher.io) {
            val analyticGoogleJson = JSONObject()
            analyticGoogleJson.put("udid",uuid.getUuid())
            analyticGoogleJson.put("webinfo", analyticGoogle)
            when (val apiData = analyticRepo.setGoogleData(analyticGoogleJson)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        ActionResult.Success(it.status.toString())
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    Log.i("ErrorCall", "invoke: ${apiData.errors}")
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}