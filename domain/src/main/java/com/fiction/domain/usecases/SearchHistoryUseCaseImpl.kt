package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.SearchHistoryUseCase
import com.fiction.domain.model.SearchBookWithTag
import com.fiction.domain.model.SearchMainItem
import com.fiction.domain.repository.SearchBookRepository
import kotlinx.coroutines.withContext

class SearchHistoryUseCaseImpl(
    private val searchHistoryRepo: SearchBookRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SearchHistoryUseCase {

    override suspend fun invoke(): ActionResult<List<SearchMainItem>> =
        withContext(dispatcher.io) {
            when (val apiData = searchHistoryRepo.getSearchHistory()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { searchedList ->
                        val historyModel = searchedList.map { SearchBookWithTag.fromHistory(it) }
                        ActionResult.Success(historyModel)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}