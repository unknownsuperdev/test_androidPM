package com.fiction.domain.repository

import com.fiction.entities.roommodels.BooksEntity
import com.fiction.entities.roommodels.ChapterEntity

interface BookChapterRepo {
    suspend fun insertBook(item: BooksEntity)
    suspend fun insertChapter(item: ChapterEntity)
    suspend fun getChapter(chapterId: Long): ChapterEntity?
    suspend fun getLastReadChapterInfo(bookId: Long): BooksEntity?
}