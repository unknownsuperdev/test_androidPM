package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetBookChaptersUseCase
import com.fiction.domain.model.ChaptersDataModel
import com.fiction.domain.repository.BookSummeryRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetBookChaptersUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetBookChaptersUseCase {
    private var chapters: List<ChaptersDataModel> = emptyList()
    private var bookId = -1L

    override suspend fun invoke(
        currentBookId: Long,
        lastReadChapterId: Long,
        isPurchased: Boolean?,
        isClear: Boolean
    ): ActionResult<List<ChaptersDataModel>> =
        withContext(dispatcher.io) {
            if (isClear) {
                chapters = emptyList()
                return@withContext ActionResult.Success(chapters)
            }
            if (lastReadChapterId != -1L) updateChapters(lastReadChapterId, isPurchased)
            else if (bookId == currentBookId) ActionResult.Success(chapters)
            else when (val apiData = bookSummeryRepo.getBookInfoById(currentBookId)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { bookResponse ->
                        val response = bookResponse.book?.chapters
                        chapters =
                            response?.map {
                                ChaptersDataModel.from(
                                    it,
                                    bookResponse.progress?.chapterId
                                )
                            } ?: emptyList()
                        bookId = currentBookId
                        ActionResult.Success(chapters)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }

    private fun updateChapters(
        lastReadChapterId: Long,
        isPurchased: Boolean?
    ): ActionResult<List<ChaptersDataModel>> {
        chapters = chapters.map {
            isPurchased?.let { isPurchased ->
                if (it.id == lastReadChapterId) it.copy(
                    isLastReadChapter = true,
                    isPurchased = isPurchased
                ) else it.copy(isLastReadChapter = false)
            } ?: if (it.id == lastReadChapterId) it.copy(isLastReadChapter = true) else it.copy(
                isLastReadChapter = false
            )
        }
        return ActionResult.Success(chapters)
    }
}