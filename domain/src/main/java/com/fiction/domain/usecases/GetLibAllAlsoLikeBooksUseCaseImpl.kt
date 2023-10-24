package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.GetLibAllAlsoLikeBooksUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.repository.LibraryRepository
import kotlinx.coroutines.withContext

class GetLibAllAlsoLikeBooksUseCaseImpl(
    private val libraryRepository: LibraryRepository,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetLibAllAlsoLikeBooksUseCase {

    override suspend fun invoke(): ActionResult<List<BookInfoAdapterModel>> =
        withContext(dispatcher.io) {
            when (val apiData = libraryRepository.getLibAlsoLikeBooks()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        val imgValue = response.map { it.covers?.imgCover }
                        cachingImages.invoke(imgValue)
                        val books = response.map {
                            BookInfoAdapterModel.from(
                                it,
                                getCachingImages(it.covers?.imgCover),
                                getCachingImages(it.covers?.imgSummary),
                                getCachingImages(it.author?.avatar)
                            )
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