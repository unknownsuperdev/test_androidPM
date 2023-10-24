package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.domain.utils.Constants.Companion.PAGE_SIZE
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BooksResponse

interface LibraryRepoWithoutPagingRepo {
    suspend fun getCurrentReadingBooks(
        page: Int = 1,
        limit: Int = PAGE_SIZE
    ): ActionResult<BaseResultModel<BooksResponse>>

    suspend fun getLibraryBooks(
        page: Int = 1,
        limit: Int = PAGE_SIZE
    ): ActionResult<BaseResultModel<BooksResponse>>

    suspend fun getFinishedBooks(
        page: Int = 1,
        limit: Int = PAGE_SIZE
    ): ActionResult<BaseResultModel<BooksResponse>>
}