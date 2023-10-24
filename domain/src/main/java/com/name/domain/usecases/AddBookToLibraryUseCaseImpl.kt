package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants
import com.name.domain.interactors.AddBookToLibraryUseCase
import com.name.domain.repository.BookStateRepo
import kotlinx.coroutines.withContext

class AddBookToLibraryUseCaseImpl(
    private val bookStateRepo: BookStateRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : AddBookToLibraryUseCase {
    override suspend fun invoke(bookId: Long): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = bookStateRepo.addBookToLibrary(bookId)) {
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