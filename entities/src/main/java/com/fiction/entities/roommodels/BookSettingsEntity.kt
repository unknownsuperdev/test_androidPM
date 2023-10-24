package com.fiction.entities.roommodels

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fiction.entities.roommodels.enums.ColorThemeEntity
import com.fiction.entities.roommodels.enums.FlipTypeEntity
import com.fiction.entities.roommodels.enums.LineHeightEntity
import com.fiction.entities.roommodels.enums.TextTypeEntity


@Entity(tableName = "bookSettings")
data class BookSettingsEntity(
    @PrimaryKey()
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "brightness")
    val brightness: Int,
    @ColumnInfo(name = "colorTheme")
    val colorTheme: ColorThemeEntity,
    @ColumnInfo(name = "textType")
    val textType: TextTypeEntity,
    @ColumnInfo(name = "textSize")
    val textSize: Int,
    @ColumnInfo(name = "lineHeight")
    val lineHeight: LineHeightEntity,
    @ColumnInfo(name = "flipType")
    val flipType: FlipTypeEntity,
)


