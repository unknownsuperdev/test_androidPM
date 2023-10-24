package com.fiction.domain.interactors

import com.fiction.domain.model.ChapterInfo

interface InsertChapterUseCase {
    suspend operator fun invoke(chapter: ChapterInfo)
}