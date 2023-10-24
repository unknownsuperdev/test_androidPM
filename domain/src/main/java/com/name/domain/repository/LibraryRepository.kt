package com.name.domain.repository

import com.name.core.ActionResult
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.SuggestedBooksItemResponse

interface LibraryRepository {

    suspend fun getBooksLibrary(): ActionResult<BaseResultModel<List<SuggestedBooksItemResponse>>>
}