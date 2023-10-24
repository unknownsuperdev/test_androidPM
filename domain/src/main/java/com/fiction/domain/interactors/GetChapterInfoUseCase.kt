package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.ChapterInfo

interface GetChapterInfoUseCase {
    suspend operator fun invoke(
        bookId: Long,
        chapterId: Long
    ): ActionResult<Pair<ChapterInfo, Int>>
}