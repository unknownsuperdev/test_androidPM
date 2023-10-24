package com.fiction.domain.model

import android.os.Parcelable
import com.fiction.domain.extentions.toData
import com.fiction.domain.extentions.toEntity
import com.fiction.domain.model.enums.ColorThemeData
import com.fiction.domain.model.enums.FlipTypeData
import com.fiction.domain.model.enums.LineHeightData
import com.fiction.domain.model.enums.TextTypeData
import com.fiction.entities.roommodels.BookSettingsEntity
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class BookSettingsData(
    val id: String,
    val brightness: Int,
    val colorTheme: ColorThemeData,
    val textType: TextTypeData,
    val textSize: Int,
    val lineHeight: LineHeightData,
    val flipType: FlipTypeData,
) : Parcelable {
    companion object {

        fun from(data: BookSettingsEntity): BookSettingsData =
            with(data) {
                BookSettingsData(
                    id,
                    brightness,
                    colorTheme.toData(),
                    textType.toData(),
                    textSize,
                    lineHeight.toData(),
                    flipType.toData()
                )
            }

        fun toEntity(data: BookSettingsData): BookSettingsEntity =
            with(data) {
                BookSettingsEntity(
                    id,
                    brightness,
                    colorTheme.toEntity(),
                    textType.toEntity(),
                    textSize,
                    lineHeight.toEntity(),
                    flipType.toEntity()
                )
            }

        fun defaultItem() = BookSettingsData(
            UUID.randomUUID().toString(),
            0,
            ColorThemeData.BLACK,
            TextTypeData.PT_SERIF,
            16,
            LineHeightData.DEFAULT,
            FlipTypeData.SCROLL
        )
    }
}
