package com.fiction.data.dataservice.apiservice

import android.os.Build
import com.fiction.data.BuildConfig
import com.fiction.entities.request.launch.LaunchRequest
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.reader.ResponseMessage
import com.fiction.entities.response.registration.PushToken
import com.fiction.entities.response.registration.TokenResponse
import com.fiction.entities.response.registration.forgotpassword.*
import com.fiction.entities.response.registration.verification.TokenRequest
import com.fiction.entities.response.updatedevice.UpdateDeviceRequest
import retrofit2.Response
import retrofit2.http.*

interface AuthService {

    @POST("v1/device/update")
    suspend fun updateDevice(
        @Body DeviceRequest: UpdateDeviceRequest,
    ): Response<BaseResultModel<ResponseMessage>>

    @POST("v1/device/push-token")
    suspend fun setPushToken(
        @Body pushToken: PushToken,
    ): Response<BaseResultModel<ResponseMessage>>

    @POST("v1/register")
    suspend fun getRegisterToken(
        @Query("platform") platform: String = "android",
        @Query("platformVersion") platformVersion: String = Build.VERSION.RELEASE,
        @Query("appVersion") appVersion: String = BuildConfig.VERSION_NAME,
        @Query("deviceName") deviceName: String = Build.MODEL,
        @Query("udid") udid: String,
        @Query("email") email: String,
        @Query("password") password: String,
    ): Response<BaseResultModel<TokenResponse>>

    @POST("v1/login")
    suspend fun getLoginToken(
        @Query("platform") platform: String = "android",
        @Query("platformVersion") platformVersion: String = Build.VERSION.RELEASE,
        @Query("appVersion") appVersion: String = BuildConfig.VERSION_NAME,
        @Query("deviceName") deviceName: String = Build.MODEL,
        @Query("udid") udid: String,
        @Query("email") email: String,
        @Query("password") password: String,
    ): Response<BaseResultModel<TokenResponse>>

    @POST("v1/facebook")
    suspend fun loginWithFacebook(
        @Query("platform") platform: String = "android",
        @Query("platformVersion") platformVersion: String = Build.VERSION.RELEASE,
        @Query("appVersion") appVersion: String = BuildConfig.VERSION_NAME,
        @Query("deviceName") deviceName: String = Build.MODEL,
        @Query("udid") udid: String,
        @Query("identifier") identifier: String,
    ): Response<BaseResultModel<TokenResponse>>

    @POST("v1/google")
    suspend fun loginWithGoogle(
        @Query("platform") platform: String = "android",
        @Query("platformVersion") platformVersion: String = Build.VERSION.RELEASE,
        @Query("appVersion") appVersion: String = BuildConfig.VERSION_NAME,
        @Query("deviceName") deviceName: String = Build.MODEL,
        @Query("udid") udid: String,
        @Query("identifier") identifier: String,
    ): Response<BaseResultModel<TokenResponse>>

    @POST("v1/apple")
    suspend fun loginWithApple(
        @Query("platform") platform: String = "android",
        @Query("platformVersion") platformVersion: String = Build.VERSION.RELEASE,
        @Query("appVersion") appVersion: String = BuildConfig.VERSION_NAME,
        @Query("deviceName") deviceName: String = Build.MODEL,
        @Query("udid") udid: String,
        @Query("identifier") identifier: String,
    ): Response<BaseResultModel<TokenResponse>>

    @GET("v1/logout")
    suspend fun logout(): Response<BaseResultModel<ResponseMessage>>

    @POST("v1/forgot-password")
    suspend fun forgotPassword(
       @Body forgotPasswordRequest: ForgotPasswordRequest
    ): Response<BaseResultModel<ForgotPasswordResponse>>

    @POST("v1/reset-password/check-code")
    suspend fun checkResetCode(
        @Body checkResetCodeRequest: CheckResetCodeRequest
    ): Response<BaseResultModel<CheckResetCodeResponse>>

    @POST("v1/reset-password")
    suspend fun resetPassword(
        @Body resetPasswordRequest: ResetPasswordRequest
    ): Response<BaseResultModel<MessageResponse>>

    @POST("v1/register/resend-email")
    suspend fun resendEmail(
        @Body token: TokenRequest
    ): Response<BaseResultModel<MessageResponse>>

}

