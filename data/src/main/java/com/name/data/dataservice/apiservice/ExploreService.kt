package com.name.data.dataservice.apiservice

import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.ExploreDataItemResponse
import retrofit2.Response
import retrofit2.http.GET

interface ExploreService {
    @GET("api/v1/feed/")
    suspend fun getExploreData() : Response<BaseResultModel<List<ExploreDataItemResponse>>>
}