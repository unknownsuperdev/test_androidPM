package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetChapterFromDBUseCase
import com.fiction.domain.model.ChapterInfo
import com.fiction.domain.repository.BookChapterRepo

class GetChapterFromDBUseCaseImp(
    private val bookChapterRepo: BookChapterRepo
) : GetChapterFromDBUseCase {

    override suspend fun invoke(bookId: Long, chapterId: Long): Pair<ChapterInfo, Int>? {
        return bookChapterRepo.getChapter(chapterId)?.let { response ->
            val chapterText = ChapterInfo.toData(response)
            val lastChapterInfo = bookChapterRepo.getLastReadChapterInfo(bookId)
            val lastReadPage =
                if (lastChapterInfo?.lastReadChapterId == chapterId) lastChapterInfo.lastReadPage else 0
            Pair(chapterText, lastReadPage ?: 0)
        }
    }
}