package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.AnalyticService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.AnalyticRepo
import com.fiction.entities.request.analytic.*
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.analytic.AnalyticResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject

class AnalyticRepoImpl(
    private val analyticService: AnalyticService
) : AnalyticRepo {

    override suspend fun setAppleData(appleDataRequest: AnalyticAppleRequest): ActionResult<BaseResultModel<AnalyticResponse>> =
        makeApiCall {
            analyzeResponse(
                analyticService.setAppleData(
                    appleDataRequest
                )
            )
        }

    override suspend fun setAppsflyerData(appsflyerRequest: AnalyticAppsflyerRequest): ActionResult<BaseResultModel<AnalyticResponse>> =
        makeApiCall {
            analyzeResponse(
                analyticService.setAppsflyerData(
                    appsflyerRequest
                )
            )
        }

    override suspend fun setFacebookData(facebookRequest: JSONObject): ActionResult<BaseResultModel<AnalyticResponse>> =
        makeApiCall {
            val fbData = JSONObject(facebookRequest.toString()).toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            analyzeResponse(
                analyticService.setFacebookData(
                    fbData
                )
            )
        }

    override suspend fun setAdjustData(adjustData: AnalyticAdjustRequest): ActionResult<BaseResultModel<AnalyticResponse>> =
        makeApiCall {
            analyzeResponse(
                analyticService.setAdjustData(
                    adjustData
                )
            )
        }

    override suspend fun setGoogleData(googleData: JSONObject): ActionResult<BaseResultModel<AnalyticResponse>> =
        makeApiCall {
            val googleData = JSONObject(googleData.toString()).toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            analyzeResponse(
                analyticService.setGoogleData(
                    googleData
                )
            )
        }

    override suspend fun setTiktokData(tiktokRequest: JSONObject): ActionResult<BaseResultModel<AnalyticResponse>> =
        makeApiCall {
            val tikTokData = JSONObject(tiktokRequest.toString()).toString()
                .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
            analyzeResponse(
                analyticService.setTiktokData(
                    tikTokData
                )
            )
        }

    override suspend fun setAndroidData(gaid: GaidRequestBody): ActionResult<BaseResultModel<AnalyticResponse>> =
        makeApiCall {
            analyzeResponse(
                analyticService.setAndroidData(
                    gaid
                )
            )
        }
}