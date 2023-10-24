package com.fiction.data.dataservice.sqlservice

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.fiction.entities.roommodels.enums.ColorThemeEntity
import com.fiction.entities.roommodels.enums.FlipTypeEntity
import com.fiction.entities.roommodels.enums.LineHeightEntity
import com.fiction.entities.roommodels.enums.TextTypeEntity


class Converters {

    @TypeConverter
    fun toColorTheme(value: String) = enumValueOf<ColorThemeEntity>(value)

    @TypeConverter
    fun fromColorTheme(value: ColorThemeEntity) = value.name

    @TypeConverter
    fun toFlipType(value: String) = enumValueOf<FlipTypeEntity>(value)

    @TypeConverter
    fun fromFlipType(value: FlipTypeEntity) = value.name

    @TypeConverter
    fun toLineHeight(value: String) = enumValueOf<LineHeightEntity>(value)

    @TypeConverter
    fun fromLineHeight(value: LineHeightEntity) = value.name

    @TypeConverter
    fun toTextType(value: String) = enumValueOf<TextTypeEntity>(value)

    @TypeConverter
    fun fromTextType(value: TextTypeEntity) = value.name

    @TypeConverter
    fun themeListToJson(value: List<Long>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToThemeList(value: String) = Gson().fromJson(value, Array<Long>::class.java).toList()

}