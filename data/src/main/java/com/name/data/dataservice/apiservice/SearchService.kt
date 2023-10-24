package com.name.data.dataservice.apiservice

import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.SearchBookItemResponse
import com.name.entities.responce.explore.SearchTagHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchService {

    @POST("api/v1/search")
    suspend fun getSearchBookList(
        @Query("name") name: String
    ): Response<BaseResultModel<SearchBookItemResponse>>

    @GET("api/v1/search/history")
    suspend fun getSearchHistoryList(): Response<BaseResultModel<SearchTagHistoryResponse>>

    @POST("api/v1/search/history/clean")
    suspend fun cleanSearchHistoryList(): Response<BaseResultModel<Any>>

}