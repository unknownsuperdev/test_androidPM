package com.fiction.domain.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.domain.utils.Constants.Companion.DEFAULT_PAGE_SIZE
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse

interface LibraryRepository {

    suspend fun getBooksLibrary(pageSize: Int = DEFAULT_PAGE_SIZE): Pager<Int, BookItemResponse>

    suspend fun getBrowsingHistoryBooks(): Pager<Int, BookItemResponse>

    suspend fun getCurrentReadingBooks(pageSize: Int = DEFAULT_PAGE_SIZE): Pager<Int, BookItemResponse>

    suspend fun getLibAlsoLikeBooks(): ActionResult<BaseResultModel<List<BookItemResponse>>>

    suspend fun getFinishedReadBooks(pageSize: Int = DEFAULT_PAGE_SIZE): Pager<Int, BookItemResponse>
}