package com.fiction.data.dataservice.apiservice

import com.fiction.entities.request.analytic.*
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.analytic.AnalyticResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface AnalyticService {

    @POST("v1/analytic/set-apple-data")
    suspend fun setAppleData(
        @Body appleDataRequest: AnalyticAppleRequest
    ): Response<BaseResultModel<AnalyticResponse>>

    @POST("v1/analytic/set-appsflyer-data")
    suspend fun setAppsflyerData(
        @Body appsflyerRequest: AnalyticAppsflyerRequest
    ): Response<BaseResultModel<AnalyticResponse>>

    @POST("v1/analytic/set-facebook-data")
    suspend fun setFacebookData(
        @Body facebookRequest: RequestBody
    ): Response<BaseResultModel<AnalyticResponse>>

    @POST("v1/analytic/set-adjust-data")
    suspend fun setAdjustData(
        @Body adjustData: AnalyticAdjustRequest
    ): Response<BaseResultModel<AnalyticResponse>>

    @Headers( "Content-Type: application/json; charset=utf-8")
    @POST("v1/analytic/set-google-data")
    suspend fun setGoogleData(
        @Body googleData: RequestBody
    ): Response<BaseResultModel<AnalyticResponse>>

    @Headers("Content-Type: application/json")
    @POST("v1/analytic/set-tiktok-data")
    suspend fun setTiktokData(
        @Body tiktokRequest: RequestBody
    ): Response<BaseResultModel<AnalyticResponse>>

    @POST("v1/analytic/set-android-data/")
    suspend fun setAndroidData(
        @Body gaid: GaidRequestBody
    ): Response<BaseResultModel<AnalyticResponse>>

}