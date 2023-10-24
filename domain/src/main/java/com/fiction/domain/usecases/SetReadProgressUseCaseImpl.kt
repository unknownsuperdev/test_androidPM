package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.SetReadProgressUseCase
import com.fiction.domain.repository.BookSummeryRepo
import com.fiction.entities.response.explore.ReadProgress
import kotlinx.coroutines.withContext

class SetReadProgressUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : SetReadProgressUseCase {
    override suspend fun invoke(
        chapterId: Long,
        percent: Double
    ) =
        withContext(dispatcher.io) {
            when (val apiData =
                bookSummeryRepo.setReadProgress(chapterId, ReadProgress(percent))) {
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