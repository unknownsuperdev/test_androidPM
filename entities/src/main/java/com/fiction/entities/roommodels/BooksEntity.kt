package com.fiction.entities.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "books")
data class BooksEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "book_id")
    val bookId: Long,
    @ColumnInfo(name = "last_read_page")
    val lastReadPage: Int?,
    @ColumnInfo(name = "last_read_chapter_id")
    val lastReadChapterId: Long?
)
