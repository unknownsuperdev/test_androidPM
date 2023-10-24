package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.reader.ReaderAnalyticsParamsRequest
import com.fiction.entities.response.reader.ReportParamsRequest
import com.fiction.entities.response.reader.ResponseMessage

interface ReaderRepo {
    suspend fun getBookInfoById(bookId: Long): ActionResult<BaseResultModel<Any>>

    suspend fun sendReport(
        bookChapterId: Long,
        reportParams: ReportParamsRequest
    ): ActionResult<BaseResultModel<ResponseMessage>>

    suspend fun sendReaderAnalytics(
        readerInfoParams: ReaderAnalyticsParamsRequest,
    ): ActionResult<BaseResultModel<Unit>>
}