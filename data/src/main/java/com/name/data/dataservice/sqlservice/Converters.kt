package com.name.data.dataservice.sqlservice

import androidx.room.TypeConverter
import com.name.entities.roommodels.enums.ColorThemeEntity
import com.name.entities.roommodels.enums.FlipTypeEntity
import com.name.entities.roommodels.enums.LineHeightEntity
import com.name.entities.roommodels.enums.TextTypeEntity


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

}