package com.fiction.data.repository

import androidx.paging.Pager
import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.SearchService
import com.fiction.data.dataservice.sqlservice.ImagesDao
import com.fiction.data.pagingsource.BooksSection
import com.fiction.data.pagingsource.SearchBooksPagingSource
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.getPagerConfig
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.SearchBookRepository
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.explore.BookItemResponse
import com.fiction.entities.response.explore.SearchBookItemResponse
import com.fiction.entities.response.explore.SearchTagHistoryResponse
import com.fiction.entities.response.reader.ResponseMessage

class SearchBookRepositoryImpl(
    private val searchService: SearchService,
    private val imagesDao: ImagesDao
) : SearchBookRepository {

    override suspend fun getSearchBookList(word: String): ActionResult<BaseResultModel<SearchBookItemResponse>> =
        makeApiCall {
            analyzeResponse(
                searchService.getSearchBookList(word)
            )
        }

    override suspend fun cleanSearchHistory(): ActionResult<BaseResultModel<ResponseMessage>> =
        makeApiCall {
            analyzeResponse(searchService.cleanSearchHistoryList())
        }

    override suspend fun getSearchHistory(): ActionResult<BaseResultModel<List<SearchTagHistoryResponse>>> =
        makeApiCall {
            analyzeResponse(searchService.getSearchHistoryList())
        }

    override suspend fun getSearchBrowsingHistoryBooks(word: String): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig()) {
            SearchBooksPagingSource.Factory()
                .create(searchService, BooksSection.BROWSING_HISTORY, word, imagesDao)
        }

    override suspend fun getSearchLibraryBooks(word: String): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig()) {
            SearchBooksPagingSource.Factory()
                .create(searchService, BooksSection.ADDED_LIBRARY, word, imagesDao)
        }

    override suspend fun getSearchFinishedBooks(word: String): Pager<Int, BookItemResponse> =
        Pager(getPagerConfig()) {
            SearchBooksPagingSource.Factory()
                .create(searchService, BooksSection.FINISHED, word, imagesDao)
        }

}
