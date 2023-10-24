package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetCurrentReadingBooksUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.domain.repository.LibraryRepoWithoutPagingRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetCurrentReadingBooksUseCaseImpl(
    private val libraryRepo: LibraryRepoWithoutPagingRepo,
    private val dispatcher: CoroutineDispatcherProvider,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase
) : GetCurrentReadingBooksUseCase {
    private var currentReadBooks: List<BooksWithReadProgressBookData>? = null

    override suspend fun invoke(isMakeCallAnyway: Boolean, isClear: Boolean): ActionResult<List<BooksWithReadProgressBookData>> =
        withContext(dispatcher.io) {
            if (isClear) {
                currentReadBooks = null
                return@withContext ActionResult.Success(emptyList())
            }
            if (currentReadBooks != null && !isMakeCallAnyway) ActionResult.Success(
                currentReadBooks ?: emptyList()
            )
            else
                when (val apiData = libraryRepo.getCurrentReadingBooks()) {
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
                            currentReadBooks = if (books.size > 5) {
                                books.subList(0, 5)
                            } else books
                            ActionResult.Success(currentReadBooks ?: emptyList())
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }

    override fun getCachedData(): List<BooksWithReadProgressBookData> = currentReadBooks ?: listOf()
}