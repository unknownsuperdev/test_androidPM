package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetMostPopularBooksUseCase
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.RemoveLikeUseCase
import com.fiction.domain.repository.BookSummeryRepo
import kotlinx.coroutines.withContext

class RemoveLikeUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val exploreDataUseCase: ExploreDataUseCase,
    private val mostPopularBooksUseCase: GetMostPopularBooksUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : RemoveLikeUseCase {

    override suspend fun invoke(bookId: Long, likeCount: Int): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = bookSummeryRepo.removeLike(bookId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        val updatedLikeCount = if (likeCount > 0) likeCount - 1 else likeCount
                        exploreDataUseCase.updateExploreData(bookId,isAddedToLib = null, isLiked = false, likeCount = updatedLikeCount)
                        mostPopularBooksUseCase(bookId, isSaved = null, isLiked = false, likesCount = updatedLikeCount)
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
