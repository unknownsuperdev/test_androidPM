package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetLibraryBooksUseCase
import com.fiction.domain.interactors.GetMostPopularBooksUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.repository.BookStateRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RemoveBookFromLibraryUseCaseImpl(
    private val bookStateRepo: BookStateRepo,
    private val dispatcher: CoroutineDispatcherProvider,
    private val exploreDataUseCase: ExploreDataUseCase,
    private val getLibraryBooksUseCase: GetLibraryBooksUseCase,
    private val mostPopularBooksUseCase: GetMostPopularBooksUseCase
) : RemoveBookFromLibraryUseCase {

    override suspend fun invoke(bookId: Long): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = bookStateRepo.removeBookFromLibrary(bookId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        exploreDataUseCase.updateExploreData(bookId, false)
                        mostPopularBooksUseCase(bookId, isSaved = false, isLiked = null, likesCount = null)
                        CoroutineScope(Dispatchers.IO).launch {
                            getLibraryBooksUseCase(true)
                        }
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}