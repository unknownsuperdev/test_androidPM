package com.fiction.data.repository

import com.fiction.data.dataservice.sqlservice.BookChapterDao
import com.fiction.domain.repository.BookChapterRepo
import com.fiction.entities.roommodels.BooksEntity
import com.fiction.entities.roommodels.ChapterEntity

class BookChapterRepoImpl(
    private val bookChapterDao: BookChapterDao
) : BookChapterRepo {
    override suspend fun insertBook(item: BooksEntity) {
        bookChapterDao.insertBook(item)
    }

    override suspend fun insertChapter(item: ChapterEntity) {
        bookChapterDao.insertChapter(item)
    }

    override suspend fun getChapter(chapterId: Long): ChapterEntity =
        bookChapterDao.getChapter(chapterId)

    override suspend fun getLastReadChapterInfo(bookId: Long): BooksEntity =
        bookChapterDao.getLastReadChapterInfo(bookId)
}