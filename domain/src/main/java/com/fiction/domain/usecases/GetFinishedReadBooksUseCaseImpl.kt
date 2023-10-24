package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetFinishedReadBooksUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.domain.repository.LibraryRepoWithoutPagingRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetFinishedReadBooksUseCaseImpl(
    private val libraryRepo: LibraryRepoWithoutPagingRepo,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val cachingImages: CachingBookImagesUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetFinishedReadBooksUseCase {

    private var finishedBooks: List<BooksWithReadProgressBookData>? = null

    override suspend fun invoke(isMakeCallAnyway: Boolean, isClear: Boolean): ActionResult<List<BooksWithReadProgressBookData>> =
        withContext(dispatcher.io) {
            if (isClear) {
                finishedBooks = null
                return@withContext ActionResult.Success(emptyList())
            }
            if (finishedBooks != null && !isMakeCallAnyway) ActionResult.Success(
                finishedBooks ?: emptyList()
            )
            else
                when (val apiData = libraryRepo.getFinishedBooks()) {
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
                            finishedBooks = if (books.size > 5) {
                                books.subList(0, 5)
                            } else books
                            ActionResult.Success(finishedBooks ?: emptyList())
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }

    override fun getCacheData(): List<BooksWithReadProgressBookData> = finishedBooks ?: listOf()
}