package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse

interface FinishedReadBooksRepo {
    suspend fun getFinishedReadBooks(): ActionResult<BaseResultModel<List<BookItemResponse>>>
}