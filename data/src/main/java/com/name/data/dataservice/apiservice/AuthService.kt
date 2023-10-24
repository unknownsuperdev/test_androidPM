package com.name.data.dataservice.apiservice

import android.os.Build
import com.name.data.BuildConfig
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.registration.GuestTokenResponse
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthService {

    @POST("api/v1/launch")
    suspend fun getGuestToken(
        @Query("platform") platform: String = "android",
        @Query("platformVersion") platformVersion: String = Build.VERSION.RELEASE,
        @Query("appVersion") appVersion: String = BuildConfig.VERSION_NAME,
        @Query("deviceName") deviceName: String = Build.MODEL,
        @Query("udid") udid: String,
    ): Response<BaseResultModel<GuestTokenResponse>>
}
