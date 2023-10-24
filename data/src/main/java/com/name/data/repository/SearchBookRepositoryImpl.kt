package com.name.data.repository

import com.name.core.ActionResult
import com.name.data.dataservice.apiservice.SearchService
import com.name.data.util.analyzeResponse
import com.name.data.util.makeApiCall
import com.name.domain.repository.SearchBookRepository
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.SearchBookItemResponse
import com.name.entities.responce.explore.SearchTagHistoryResponse

class SearchBookRepositoryImpl(
    private val searchService: SearchService
) : SearchBookRepository {

    override suspend fun getSearchBookList(word: String): ActionResult<BaseResultModel<SearchBookItemResponse>> =
        makeApiCall {
            analyzeResponse(
                searchService.getSearchBookList(word)
            )
        }

    override suspend fun cleanSearchHistory(): ActionResult<BaseResultModel<Any>> =
        makeApiCall {
            analyzeResponse(searchService.cleanSearchHistoryList())
        }

    override suspend fun getSearchHistory(): ActionResult<BaseResultModel<SearchTagHistoryResponse>> =
        makeApiCall {
            analyzeResponse(searchService.getSearchHistoryList())
        }
}
