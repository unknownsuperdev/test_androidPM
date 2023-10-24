package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.CleanSearchHistoryUseCase
import com.fiction.domain.repository.SearchBookRepository
import kotlinx.coroutines.withContext

class CleanSearchHistoryUseCaseImpl(
    private val cleanSearchHistoryRepo: SearchBookRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : CleanSearchHistoryUseCase {

    override suspend fun invoke(): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = cleanSearchHistoryRepo.cleanSearchHistory()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}