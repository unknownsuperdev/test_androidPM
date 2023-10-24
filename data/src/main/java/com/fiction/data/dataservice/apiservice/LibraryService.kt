package com.fiction.data.dataservice.apiservice

import com.fiction.data.util.Constants.Companion.DEFAULT_PAGE_SIZE
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LibraryService {

    @GET("/v1/library/books")
    suspend fun getLibraryBooks(
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("/v1/library/books/browsing-history")
    suspend fun getBrowsingHistoryBooks(
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("v1/library/books/reading")
    suspend fun getCurrentReadingBooks(
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("/v1/library/books/finished")
    suspend fun getFinishedBooks(
        @Query("page") page: Int,
        @Query("limit") limit: Int = DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("/v1/library/books/also-like")
    suspend fun getLibAlsoLikeBooks(): Response<BaseResultModel<List<BookItemResponse>>>

}
