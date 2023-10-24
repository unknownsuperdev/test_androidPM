package com.fiction.data.dataservice.apiservice

import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.reader.ReaderAnalyticsParamsRequest
import com.fiction.entities.response.reader.ReportParamsRequest
import com.fiction.entities.response.reader.ResponseMessage
import retrofit2.Response
import retrofit2.http.*

interface ReaderApiService {

    @POST("v1/books/{book_id}/view/set")
    suspend fun getBookInfoById(@Path("book_id") bookId: Long): Response<BaseResultModel<Any>>

    @POST("v1/books/chapters/{book_chapter_id}/send-report")
    suspend fun sendReport(
        @Path("book_chapter_id") bookChapterId: Long,
        @Body reportParams: ReportParamsRequest
    ): Response<BaseResultModel<ResponseMessage>>

    @PUT("v1/sessions/reads")
    suspend fun sendReaderAnalytics(
        @Body readerInfoParams: ReaderAnalyticsParamsRequest,
    ): Response<BaseResultModel<Unit>>
}