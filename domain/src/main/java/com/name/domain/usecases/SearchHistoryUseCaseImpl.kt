package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants
import com.name.domain.interactors.SearchHistoryUseCase
import com.name.domain.model.SearchBookWithTag
import com.name.domain.model.SearchMainItem
import com.name.domain.repository.SearchBookRepository
import kotlinx.coroutines.withContext

class SearchHistoryUseCaseImpl(
    private val searchHistoryRepo: SearchBookRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SearchHistoryUseCase {
    private val historyModel = mutableListOf<SearchMainItem>()

    override suspend fun invoke(): ActionResult<List<SearchMainItem>> =
        withContext(dispatcher.io) {
            when (val apiData = searchHistoryRepo.getSearchHistory()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        historyModel.add(SearchBookWithTag.fromHistory(it))
                        ActionResult.Success(historyModel)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}