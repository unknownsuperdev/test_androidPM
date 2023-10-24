package com.name.data.dataservice.apiservice

import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.DataInfoResponse
import com.name.entities.responce.explore.SearchBookItemResponse
import com.name.entities.responce.registration.GuestTokenResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AllApiService {

    @GET("get_memes")
    suspend fun getTestData(): Response<BaseResultModel<List<DataInfoResponse>>>

    @POST("api/v1/user/ghost")
    suspend fun getGuestToken(): Response<BaseResultModel<GuestTokenResponse>>
}
