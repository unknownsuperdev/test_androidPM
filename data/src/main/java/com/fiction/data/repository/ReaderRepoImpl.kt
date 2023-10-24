package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.ReaderApiService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.ReaderRepo
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.reader.ReaderAnalyticsParamsRequest
import com.fiction.entities.response.reader.ReportParamsRequest
import com.fiction.entities.response.reader.ResponseMessage

class ReaderRepoImpl(
    private val readerApiService: ReaderApiService
) : ReaderRepo {

    override suspend fun getBookInfoById(bookId: Long): ActionResult<BaseResultModel<Any>> =
        makeApiCall {
            analyzeResponse(
                readerApiService.getBookInfoById(bookId)
            )
        }

    override suspend fun sendReport(
        bookChapterId: Long,
        reportParams: ReportParamsRequest
    ): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(
                readerApiService.sendReport(bookChapterId, reportParams)
            )
        }

    override suspend fun sendReaderAnalytics(readerInfoParams: ReaderAnalyticsParamsRequest): ActionResult<BaseResultModel<Unit>> =
        makeApiCall {
            analyzeResponse(readerApiService.sendReaderAnalytics(readerInfoParams))
        }

}