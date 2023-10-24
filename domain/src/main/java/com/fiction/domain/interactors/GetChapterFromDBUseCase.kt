package com.fiction.domain.interactors

import com.fiction.domain.model.ChapterInfo

interface GetChapterFromDBUseCase {
    suspend operator fun invoke(bookId: Long, chapterId: Long) : Pair<ChapterInfo,Int>?
}