package com.fiction.domain.model.onboarding

import com.fiction.entities.roommodels.OnBoardingSettingsEntity
import java.util.*

data class OnBoardingSettingData(
    val id: String,
    val genderId: Int? = null,
    val readingTimeId: Int? = null,
    val favoriteThemeTags: List<Long>? = listOf(),
) {


    companion object {
        fun from(onBoardingSettingsEntity: OnBoardingSettingsEntity) =
            with(onBoardingSettingsEntity) {
                OnBoardingSettingData(
                    id,
                    genderId,
                    readingTimeId,
                    favoriteThemeTags
                )
            }

        fun toEntity(onBoardingSettingData: OnBoardingSettingData) =
            with(onBoardingSettingData) {
                OnBoardingSettingsEntity(
                    id,
                    genderId,
                    readingTimeId,
                    favoriteThemeTags
                )
            }

        fun emptyItem() =
            OnBoardingSettingData(
                UUID.randomUUID().toString(),
                null,
                null,
                null
            )

    }
}
