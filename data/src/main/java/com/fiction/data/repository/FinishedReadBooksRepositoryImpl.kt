package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.FinishedReadBooksService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.FinishedReadBooksRepo
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse

class FinishedReadBooksRepositoryImpl(private val finishedReadBooksService: FinishedReadBooksService) :
    FinishedReadBooksRepo {
    override suspend fun getFinishedReadBooks(): ActionResult<BaseResultModel<List<BookItemResponse>>> =
        makeApiCall {
            analyzeResponse(
                finishedReadBooksService.getFinishedBooks()
            )
        }
}