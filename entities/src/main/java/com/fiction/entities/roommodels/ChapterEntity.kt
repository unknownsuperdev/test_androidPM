package com.fiction.entities.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chapters")
data class ChapterEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "chapter_id")
    val chapterId: Long?,
    @ColumnInfo(name = "title")
    val title: String?,
    @ColumnInfo(name = "text")
    val text: String?,
    @ColumnInfo(name = "order")
    val order: Int?,
    @ColumnInfo(name = "book_id")
    val bookId: Long?
)
