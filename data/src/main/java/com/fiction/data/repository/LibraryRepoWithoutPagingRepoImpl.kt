package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.LibraryService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.LibraryRepoWithoutPagingRepo
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BooksResponse

class LibraryRepoWithoutPagingRepoImpl(
    private val libraryService: LibraryService
) : LibraryRepoWithoutPagingRepo {

    override suspend fun getCurrentReadingBooks(
        page: Int,
        limit: Int
    ): ActionResult<BaseResultModel<BooksResponse>> =
        makeApiCall {
            analyzeResponse(
                libraryService.getCurrentReadingBooks(page, limit)
            )
        }

    override suspend fun getLibraryBooks(
        page: Int,
        limit: Int
    ): ActionResult<BaseResultModel<BooksResponse>> =
        makeApiCall {
            analyzeResponse(
                libraryService.getLibraryBooks(page, limit)
            )
        }

    override suspend fun getFinishedBooks(
        page: Int,
        limit: Int
    ): ActionResult<BaseResultModel<BooksResponse>> =
        makeApiCall {
            analyzeResponse(
                libraryService.getFinishedBooks(page, limit)
            )
        }

}