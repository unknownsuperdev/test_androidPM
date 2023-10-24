package com.fiction.data.dataservice.apiservice

import com.fiction.data.util.Constants
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.AllGenresItemResponse
import com.fiction.entities.response.explore.BooksResponse
import com.fiction.entities.response.explore.ExploreDataItemResponse
import com.fiction.entities.response.explore.GenreResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExploreService {
    @GET("/v1/books/feed")
    suspend fun getExploreData(): Response<BaseResultModel<List<ExploreDataItemResponse>>>

    @GET("/v1/books/genres")
    suspend fun getAllGenres(): Response<BaseResultModel<List<GenreResponse>>>

    @GET("/v1/books/genres/{genre_id}")
    suspend fun getGenreById(
        @Path("genre_id") genreId: Long,
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("/v1/books/for_you")
    suspend fun getAllForYouBooks(
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("/v1/books/popular")
    suspend fun getAllPopularBooks(
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("/v1/books/new")
    suspend fun getAllNewBooks(
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>

    @GET("/v1/books/genres/{genre_id}/hot")
    suspend fun getHotBooks(
        @Path("genre_id") genre_id: Int,
        @Query("page") page: Int,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>
}