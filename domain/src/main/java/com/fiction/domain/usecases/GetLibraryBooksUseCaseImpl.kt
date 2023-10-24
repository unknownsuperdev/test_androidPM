package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.GetLibraryBooksUseCase
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.domain.repository.LibraryRepoWithoutPagingRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetLibraryBooksUseCaseImpl(
    private val libraryRepo: LibraryRepoWithoutPagingRepo,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val cachingImages: CachingBookImagesUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetLibraryBooksUseCase {
    private var libraryBooks: List<BooksWithReadProgressBookData>? = null

    override suspend fun invoke(isMakeCallAnyway: Boolean, isClear: Boolean): ActionResult<List<BooksWithReadProgressBookData>> =
        withContext(dispatcher.io) {
            if (isClear){
                libraryBooks = null
                return@withContext ActionResult.Success(emptyList())
            }
            if (libraryBooks != null && !isMakeCallAnyway) ActionResult.Success(
                libraryBooks ?: emptyList()
            )
            else
                when (val apiData = libraryRepo.getLibraryBooks()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.items?.let { response ->
                            val imgValue = response.map { it.covers?.imgCover }
                            cachingImages.invoke(imgValue)
                            val books = response.map {
                                BooksWithReadProgressBookData.from(
                                    it,
                                    getCachingImages.invoke(it.covers?.imgCover),
                                    getCachingImages(it.covers?.imgSummary),
                                    getCachingImages(it.author?.avatar)
                                )
                            }
                            libraryBooks = if (books.size > 5) {
                                books.subList(0, 5)
                            } else books
                            ActionResult.Success(libraryBooks ?: emptyList())
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }
}

