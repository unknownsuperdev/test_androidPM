package com.name.domain.repository

import com.name.core.ActionResult
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.SearchBookItemResponse
import com.name.entities.responce.explore.SearchTagHistoryResponse

interface SearchBookRepository {
    suspend fun getSearchHistory(): ActionResult<BaseResultModel<SearchTagHistoryResponse>>
    suspend fun cleanSearchHistory(): ActionResult<BaseResultModel<Any>>
    suspend fun getSearchBookList(word: String): ActionResult<BaseResultModel<SearchBookItemResponse>>
}
