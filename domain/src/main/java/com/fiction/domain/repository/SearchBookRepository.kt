package com.fiction.domain.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.SearchBookItemResponse
import com.fiction.entities.response.explore.SearchTagHistoryResponse
import com.fiction.entities.response.reader.ResponseMessage

interface SearchBookRepository {
    suspend fun getSearchHistory(): ActionResult<BaseResultModel<List<SearchTagHistoryResponse>>>
    suspend fun cleanSearchHistory(): ActionResult<BaseResultModel<ResponseMessage>>
    suspend fun getSearchBookList(word: String): ActionResult<BaseResultModel<SearchBookItemResponse>>
    suspend fun getSearchBrowsingHistoryBooks(word: String): Pager<Int, BookItemResponse>
    suspend fun getSearchLibraryBooks(word: String): Pager<Int, BookItemResponse>
    suspend fun getSearchFinishedBooks(word: String): Pager<Int, BookItemResponse>
}
