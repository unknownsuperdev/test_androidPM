package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.request.analytic.*
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.analytic.AnalyticResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body

interface AnalyticRepo {
    suspend fun setAppleData(
        appleDataRequest: AnalyticAppleRequest
    ): ActionResult<BaseResultModel<AnalyticResponse>>

    suspend fun setAppsflyerData(
        appsflyerRequest: AnalyticAppsflyerRequest
    ): ActionResult<BaseResultModel<AnalyticResponse>>

    suspend fun setFacebookData(
        facebookRequest: JSONObject
    ): ActionResult<BaseResultModel<AnalyticResponse>>

    suspend fun setAdjustData(
        adjustData: AnalyticAdjustRequest
    ): ActionResult<BaseResultModel<AnalyticResponse>>

    suspend fun setGoogleData(
        googleData: JSONObject
    ): ActionResult<BaseResultModel<AnalyticResponse>>

    suspend fun setTiktokData(
        tiktokRequest: JSONObject
    ): ActionResult<BaseResultModel<AnalyticResponse>>
    suspend fun setAndroidData(
        gaid: GaidRequestBody
    ): ActionResult<BaseResultModel<AnalyticResponse>>
}