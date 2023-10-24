package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetSuggestionBooksUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.SetImageCacheUseCase
import com.fiction.domain.model.RetentionScreenBookItem
import com.fiction.domain.repository.BookSummeryRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetSuggestionBooksUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val setImageCacheUseCase: SetImageCacheUseCase,
    private val dispatcher: CoroutineDispatcherProvider
): GetSuggestionBooksUseCase {

    private var books: List<RetentionScreenBookItem> = emptyList()
    private var currentReadBookId = -1L
    override suspend fun invoke(bookId: Long, isClearList: Boolean): ActionResult<List<RetentionScreenBookItem>> =
        withContext(dispatcher.io) {
            if (currentReadBookId == bookId) ActionResult.Success(books)
            else
                when (val apiData = bookSummeryRepo.getSuggestionBooks(bookId)) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let { response ->
                            currentReadBookId = bookId
                            val imgValue = response.map { it.covers?.imgCover }
                            cachingImages.invoke(imgValue)
                            books = response.map {
                                RetentionScreenBookItem.from(
                                    it,
                                    getCachingImages(it.covers?.imgCover)
                                )
                            }
                            books.forEach {
                                setImageCacheUseCase(it.image)
                            }
                            ActionResult.Success(books)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }
}