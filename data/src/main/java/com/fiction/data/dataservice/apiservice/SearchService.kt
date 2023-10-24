package com.fiction.data.dataservice.apiservice

import com.fiction.data.util.Constants
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BooksResponse
import com.fiction.entities.response.explore.SearchBookItemResponse
import com.fiction.entities.response.explore.SearchTagHistoryResponse
import com.fiction.entities.response.reader.ResponseMessage
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SearchService {

    @POST("v1/search")
    suspend fun getSearchBookList(
        @Query("name") name: String
    ): Response<BaseResultModel<SearchBookItemResponse>>

    @GET("v1/search/history")
    suspend fun getSearchHistoryList(): Response<BaseResultModel<List<SearchTagHistoryResponse>>>

    @POST("v1/search/history/clean")
    suspend fun cleanSearchHistoryList(): Response<BaseResultModel<ResponseMessage>>

    @POST("v1/library/books/browsing-history/search")
    suspend fun getSearchedBookFromBrowsingHistory(
        @Query("name") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @POST("v1/library/books/search")
    suspend fun getSearchedBookFromLib(
        @Query("name") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @POST("v1/library/books/finished/search")
    suspend fun getSearchedBookFromFinished(
        @Query("name") name: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

}