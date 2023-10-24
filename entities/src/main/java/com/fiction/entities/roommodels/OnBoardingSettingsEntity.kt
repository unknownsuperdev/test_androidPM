package com.fiction.entities.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "onBoardingSettings")
data class OnBoardingSettingsEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "genderId")
    val genderId: Int?,
    @ColumnInfo(name = "readingTimeId")
    val readingTimeId: Int?,
    @ColumnInfo(name = "favoriteThemeTags")
    val favoriteThemeTags: List<Long>?,
)
