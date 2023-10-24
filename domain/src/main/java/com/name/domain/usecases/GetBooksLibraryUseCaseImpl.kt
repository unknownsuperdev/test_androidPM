package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants
import com.name.domain.interactors.GetBooksLibraryUseCase
import com.name.domain.model.BooksWithReadProgressBookData
import com.name.domain.repository.LibraryRepository
import kotlinx.coroutines.withContext

class GetBooksLibraryUseCaseImpl(
    private val libraryRepository: LibraryRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GetBooksLibraryUseCase {
    override suspend fun invoke(): ActionResult<List<BooksWithReadProgressBookData>> =
        withContext(dispatcher.io) {
            when (val apiData = libraryRepository.getBooksLibrary()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        val books = response.map { BooksWithReadProgressBookData.from(it) }
                        ActionResult.Success(books)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}