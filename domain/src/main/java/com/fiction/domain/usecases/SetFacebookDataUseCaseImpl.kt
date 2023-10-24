package com.fiction.domain.usecases

import android.util.Log
import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.analytic.AnalyticFacebook
import com.fiction.domain.interactors.SetFacebookDataUseCase
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.domain.utils.Constants
import com.google.gson.Gson
import org.json.JSONObject

class SetFacebookDataUseCaseImpl(
    private val analyticRepo: AnalyticRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : SetFacebookDataUseCase {

    override suspend fun invoke(
        analyticFacebook: AnalyticFacebook,
        webInfo: JSONObject?
    ): ActionResult<String> =
        with(dispatcher.io) {
            val analyticJson = JSONObject(Gson().toJson(analyticFacebook))
            if (webInfo != null) analyticJson.put("webinfo", webInfo)
            Log.i("setAndroidData", "SetFacebookDataUseCase: fbInfo = ${analyticJson}")
            when (val apiData = analyticRepo.setFacebookData(analyticJson)) {
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