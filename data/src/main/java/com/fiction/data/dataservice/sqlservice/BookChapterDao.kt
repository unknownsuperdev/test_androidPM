package com.fiction.data.dataservice.sqlservice

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fiction.entities.roommodels.BooksEntity
import com.fiction.entities.roommodels.ChapterEntity

@Dao
interface BookChapterDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBook(item: BooksEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertChapter(item: ChapterEntity)

    @Query("SELECT * FROM chapters WHERE chapter_id = :chapterId")
    fun getChapter(chapterId: Long): ChapterEntity

    @Query("SELECT * FROM books WHERE book_id = :bookId")
    fun getLastReadChapterInfo(bookId: Long): BooksEntity

}