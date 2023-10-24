package com.fiction.data.dataservice.apiservice

import com.fiction.data.util.Constants
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.*
import com.fiction.entities.response.reader.ResponseMessage
import retrofit2.Response
import retrofit2.http.*

interface BookSummaryService {

    @GET("/v1/books/{book_id}")
    suspend fun getBookInfoById(@Path("book_id") bookId: Long): Response<BaseResultModel<BookInfoResponse>>

    @GET("v1/books/chapters/{book_chapter_id}")
    suspend fun getChapterInfoById(
        @Path("book_chapter_id") bookChapterId: Long
    ): Response<BaseResultModel<ChapterInfoResponse>>

    @POST("/v1/books/chapters/{book_chapter_id}/set-progress")
    suspend fun setReadProgress(
        @Body readProgress: ReadProgress,
        @Path("book_chapter_id") bookChapterId: Long
    ): Response<BaseResultModel<Any>>

    @POST("/v1/books/{book_id}/like/add")
    suspend fun setLike(
        @Path("book_id") bookId: Long,
    ): Response<BaseResultModel<ResponseMessage>>

    @POST("/v1/books/{book_id}/like/remove")
    suspend fun removeLike(
        @Path("book_id") bookId: Long,
    ): Response<BaseResultModel<ResponseMessage>>

    @GET("/v1/books/{book_id}/suggestions")
    suspend fun getSuggestionBooks(
        @Path("book_id") bookId: Long,
    ): Response<BaseResultModel<List<BookItemResponse>>>

    @GET("v1/books/{book_id}/also-like")
    suspend fun getAlsoLikeBooks(
        @Path("book_id") bookId: Long,
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = Constants.DEFAULT_PAGE_SIZE
    ): Response<BaseResultModel<BooksResponse>>
}