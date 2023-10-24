package com.fiction.data.dataservice.apiservice

import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.DataInfoResponse
import com.fiction.entities.response.registration.TokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface AllApiService {

    @GET("get_memes")
    suspend fun getTestData(): Response<BaseResultModel<List<DataInfoResponse>>>

    @POST("v1/user/ghost")
    suspend fun getGuestToken(): Response<BaseResultModel<TokenResponse>>
}
