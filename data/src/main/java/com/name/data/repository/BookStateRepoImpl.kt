package com.name.data.repository

import com.name.core.ActionResult
import com.name.data.dataservice.apiservice.BookStateService
import com.name.data.util.analyzeResponse
import com.name.data.util.makeApiCall
import com.name.domain.repository.BookStateRepo
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.BookTemp

class BookStateRepoImpl(
    private val bookStateService: BookStateService
) : BookStateRepo {

    override suspend fun addBookToLibrary(bookId: Long): ActionResult<BaseResultModel<String>> =
        makeApiCall {
            analyzeResponse(bookStateService.addBookToLibrary(BookTemp(bookId)))
        }

    override suspend fun removeBookFromLibrary(bookId: Long): ActionResult<BaseResultModel<String>> =
        makeApiCall {
            analyzeResponse(bookStateService.removeBookToLibrary(BookTemp(bookId)))
        }
}