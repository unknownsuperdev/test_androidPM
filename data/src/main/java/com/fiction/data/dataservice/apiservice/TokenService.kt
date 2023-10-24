package com.fiction.data.dataservice.apiservice

import com.fiction.entities.request.launch.LaunchRequest
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.registration.TokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TokenService {

    @POST("v1/launch")
    suspend fun getGuestToken(
        @Body launchRequest: LaunchRequest
    ): Response<BaseResultModel<TokenResponse>>

    @POST("v1/refresh-token")
    suspend fun updateToken(
        @Header("Authorization") token: String,
    ): Response<BaseResultModel<TokenResponse>>

}