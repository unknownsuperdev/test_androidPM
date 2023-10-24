package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.GetAlsoLikeBooksUseCase
import com.fiction.domain.interactors.SetImageCacheUseCase
import com.fiction.domain.model.BooksDataModel
import com.fiction.domain.repository.BookSummeryRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetAlsoLikeBooksUseCaseImpl(
    private val bookSumRepo: BookSummeryRepo,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val setImageCacheUseCase: SetImageCacheUseCase,
    private val dispatcher: CoroutineDispatcherProvider
): GetAlsoLikeBooksUseCase {

    override suspend fun invoke(
        bookId: Long,
        isClearList: Boolean
    ): ActionResult<List<BooksDataModel>>  =
        withContext(dispatcher.io) {
            val numberOfElements = 5
            when (val apiData = bookSumRepo.getAlsoLikeBooks(bookId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.items?.let { response ->
                        val imgValue = response.map { it.covers?.imgCover }
                        cachingImages.invoke(imgValue)
                        var books = response.map {
                            BooksDataModel.from(
                                it,
                                getCachingImages(it.covers?.imgCover),
                                getCachingImages(it.covers?.imgSummary),
                                getCachingImages(it.author?.avatar)
                            )
                        }
                        if (books.size > numberOfElements)
                            books = books.asSequence().shuffled().take(numberOfElements).toList()
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