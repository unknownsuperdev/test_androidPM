package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CachingBookImagesUseCase
import com.fiction.domain.interactors.GetImageFromCachingUseCase
import com.fiction.domain.interactors.GetMostPopularBooksUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.repository.ExploreDataRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetMostPopularBooksUseCaseImpl(
    private val exploreDataRepository: ExploreDataRepository,
    private val cachingImages: CachingBookImagesUseCase,
    private val getCachingImages: GetImageFromCachingUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : GetMostPopularBooksUseCase {
    private var popularBooks: List<BookInfoAdapterModel> = emptyList()

    override suspend fun invoke(
        id: Long,
        isSaved: Boolean?,
        isLiked: Boolean?,
        likesCount: Int?,
        isClear: Boolean
    ): ActionResult<List<BookInfoAdapterModel>> =
        withContext(dispatcher.io) {
            if (isClear) {
                popularBooks = emptyList()
                return@withContext ActionResult.Success(emptyList())
            }
            if (popularBooks.isNotEmpty() && id == -1L) ActionResult.Success(popularBooks)
            else if (id != -1L) updateBooks(id, isSaved, isLiked, likesCount)
            else
                when (val apiData = exploreDataRepository.getMostPopularBooks()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.items?.let { response ->
                            val imgValue = response.map { it.covers?.imgCover }
                            cachingImages.invoke(imgValue)
                            popularBooks = response.map {
                                BookInfoAdapterModel.from(
                                    it,
                                    getCachingImages(it.covers?.imgCover),
                                    getCachingImages(it.covers?.imgSummary),
                                    getCachingImages(it.author?.avatar)
                                )
                            }
                            ActionResult.Success(popularBooks)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }

    private fun updateBooks(
        id: Long,
        isSaved: Boolean?,
        isLiked: Boolean?,
        likesCount: Int?
    ): ActionResult<List<BookInfoAdapterModel>> {
        popularBooks = popularBooks.map { if (it.id == id) it.copy(
            isSaved = isSaved ?: it.isSaved,
            bookInfo = it.bookInfo.copy(isAddedInLib = isSaved?: it.isSaved,
                isLike = isLiked ?:  it.bookInfo.isLike,
                likes = likesCount ?: it.bookInfo.likes
            ),
            likes = likesCount ?: it.likes
        ) else it }
        return ActionResult.Success(popularBooks)
    }
}