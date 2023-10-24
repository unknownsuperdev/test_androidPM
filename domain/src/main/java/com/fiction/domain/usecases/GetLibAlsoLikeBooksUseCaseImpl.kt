package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.GetLibAlsoLikeBooksUseCase
import com.fiction.domain.model.BooksDataModel
import com.fiction.domain.repository.LibraryRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetLibAlsoLikeBooksUseCaseImpl(
    private val libraryRepository: LibraryRepository,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetLibAlsoLikeBooksUseCase {
    private var alsoLikeBooks: List<BooksDataModel>? = null
    private val numberOfElements = 5

    override suspend fun invoke(isMakeCallAnyway: Boolean, isClear: Boolean): ActionResult<List<BooksDataModel>> =
        withContext(dispatcher.io) {
            if (isClear) {
                alsoLikeBooks = null
                return@withContext ActionResult.Success(emptyList())
            }
            if (alsoLikeBooks != null && !isMakeCallAnyway) ActionResult.Success(alsoLikeBooks ?: emptyList())
            else
                when (val apiData = libraryRepository.getLibAlsoLikeBooks()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let { response ->
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
                                books =
                                    books.asSequence().shuffled().take(numberOfElements).toList()
                            alsoLikeBooks = books
                            ActionResult.Success(books)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }

    override fun getCacheData(): List<BooksDataModel> = alsoLikeBooks ?: listOf()

}