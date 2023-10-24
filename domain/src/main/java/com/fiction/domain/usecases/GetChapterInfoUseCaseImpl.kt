package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.core.utils.Encryption
import com.fiction.core.utils.Uuid
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.GetChapterFromDBUseCase
import com.fiction.domain.interactors.GetChapterInfoUseCase
import com.fiction.domain.interactors.InsertChapterUseCase
import com.fiction.domain.model.ChapterInfo
import com.fiction.domain.repository.BookSummeryRepo
import kotlinx.coroutines.withContext

class GetChapterInfoUseCaseImpl(
    private val bookSummeryRepo: BookSummeryRepo,
    private val getChapterFromDBUseCase: GetChapterFromDBUseCase,
    private val insertChapterUseCase: InsertChapterUseCase,
    private val dispatcher: CoroutineDispatcherProvider,
    private val uuid: Uuid
) : GetChapterInfoUseCase {

    override suspend fun invoke(
        bookId: Long,
        chapterId: Long
    ): ActionResult<Pair<ChapterInfo, Int>> =
        withContext(dispatcher.io) {
            val readStartPage = 0
            val chapterFromDB = getChapterFromDBUseCase(bookId, chapterId)
            if (chapterFromDB == null) {
                when (val apiData = bookSummeryRepo.getChapterInfoById(chapterId)) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let {
                            val decryptText =
                                Encryption.decryptText(uuid.getUuid(), it.title, it.text, chapterId)

                            val chapterInfo = ChapterInfo.from(it, bookId)
                                .copy(text = decryptText ?: "")
                            if (chapterInfo.isPurchased)
                                insertChapterUseCase(chapterInfo)
                            ActionResult.Success(Pair(chapterInfo, readStartPage))
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }
                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
            } else {
                ActionResult.Success(chapterFromDB)
            }
        }

}