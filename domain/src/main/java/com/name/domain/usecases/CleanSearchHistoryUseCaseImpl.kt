package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants
import com.name.domain.interactors.CleanSearchHistoryUseCase
import com.name.domain.repository.SearchBookRepository
import kotlinx.coroutines.withContext

class CleanSearchHistoryUseCaseImpl(
    private val cleanSearchHistoryRepo: SearchBookRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : CleanSearchHistoryUseCase {

    override suspend fun invoke(): ActionResult<Any> =
        withContext(dispatcher.io) {
            when (val apiData = cleanSearchHistoryRepo.cleanSearchHistory()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        ActionResult.Success(it)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}