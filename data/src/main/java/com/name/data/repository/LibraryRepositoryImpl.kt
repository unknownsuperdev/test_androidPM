package com.name.data.repository

import com.name.core.ActionResult
import com.name.data.dataservice.apiservice.LibraryService
import com.name.data.util.analyzeResponse
import com.name.data.util.makeApiCall
import com.name.domain.repository.LibraryRepository
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.SuggestedBooksItemResponse

class LibraryRepositoryImpl(private val libraryService: LibraryService) : LibraryRepository {

    override suspend fun getBooksLibrary(): ActionResult<BaseResultModel<List<SuggestedBooksItemResponse>>> =
        makeApiCall {
            analyzeResponse(libraryService.getBooksLibrary())
        }
}