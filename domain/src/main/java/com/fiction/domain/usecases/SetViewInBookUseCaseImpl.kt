package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.SetViewInBookUseCase
import com.fiction.domain.repository.ReaderRepo
import kotlinx.coroutines.withContext

class SetViewInBookUseCaseImpl(
    private val readerRepo: ReaderRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : SetViewInBookUseCase {

    override suspend fun invoke(bookId: Long): ActionResult<Any> =
        withContext(dispatcher.io) {
            when (val apiData = readerRepo.getBookInfoById(bookId)) {
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