package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants.Companion.ERROR_NULL_DATA
import com.name.domain.interactors.SearchBookUseCase
import com.name.domain.model.SearchBookWithName
import com.name.domain.model.SearchBookWithTag
import com.name.domain.model.SearchMainItem
import com.name.domain.repository.SearchBookRepository
import kotlinx.coroutines.withContext

class SearchBookUseCaseImpl(
    private val repository: SearchBookRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SearchBookUseCase {

    override suspend fun invoke(word: String): ActionResult<List<SearchMainItem>> {
        val baseSearchBookDataModel = mutableListOf<SearchMainItem>()

        return withContext(dispatcher.io) {
            when (val apiData = repository.getSearchBookList(word)) {
                is ActionResult.Success -> {

                    apiData.result.data?.let { response ->
                        response.searchBookWithName?.let { itemResponseList ->
                            itemResponseList.forEach {
                                baseSearchBookDataModel.add(SearchBookWithName.from(it))
                            }
                        }
                        response.searchBookWithTag?.let { itemResponseList ->
                            itemResponseList.forEach {
                                baseSearchBookDataModel.add(SearchBookWithTag.from(it))
                            }
                        }
                        ActionResult.Success(baseSearchBookDataModel)
                    } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
    }
}
