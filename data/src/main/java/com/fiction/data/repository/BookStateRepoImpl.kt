package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.BookStateService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.BookStateRepo
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookTemp
import com.fiction.entities.response.reader.ResponseMessage

class BookStateRepoImpl(
    private val bookStateService: BookStateService
) : BookStateRepo {

    override suspend fun addBookToLibrary(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(bookStateService.addBookToLibrary(BookTemp(bookId)))
        }

    override suspend fun removeBookFromLibrary(bookId: Long): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(bookStateService.removeBookToLibrary(BookTemp(bookId)))
        }
}