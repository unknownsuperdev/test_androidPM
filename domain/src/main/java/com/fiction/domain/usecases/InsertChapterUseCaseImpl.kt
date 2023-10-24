package com.fiction.domain.usecases

import com.fiction.domain.interactors.InsertChapterUseCase
import com.fiction.domain.model.ChapterInfo
import com.fiction.domain.repository.BookChapterRepo

class InsertChapterUseCaseImpl(
    private val bookChapterRepo: BookChapterRepo
) : InsertChapterUseCase {

    override suspend fun invoke(chapter: ChapterInfo) {
        bookChapterRepo.insertChapter(ChapterInfo.toEntity(chapter))
    }
}