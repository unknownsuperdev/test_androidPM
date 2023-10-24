package com.name.domain.model

import android.os.Parcelable
import com.name.domain.extentions.toData
import com.name.domain.extentions.toEntity
import com.name.domain.model.enums.ColorThemeData
import com.name.domain.model.enums.FlipTypeData
import com.name.domain.model.enums.LineHeightData
import com.name.domain.model.enums.TextTypeData
import com.name.entities.roommodels.BookSettingsEntity
import kotlinx.parcelize.Parcelize

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
    }
}
