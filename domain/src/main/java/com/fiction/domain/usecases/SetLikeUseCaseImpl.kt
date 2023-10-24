package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetMostPopularBooksUseCase
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.SetLikeUseCase
import com.fiction.domain.repository.BookSummeryRepo
import kotlinx.coroutines.withContext

class SetLikeUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val exploreDataUseCase: ExploreDataUseCase,
    private val mostPopularBooksUseCase: GetMostPopularBooksUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : SetLikeUseCase {

    override suspend fun invoke(bookId: Long, likeCount: Int): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = bookSummeryRepo.setLike(bookId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        val updatedLikeCount = likeCount + 1
                        exploreDataUseCase.updateExploreData(bookId,isAddedToLib = null, isLiked = true, likeCount = updatedLikeCount)
                        mostPopularBooksUseCase(bookId, isSaved = null, isLiked = true, likesCount = updatedLikeCount)
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}