package com.fiction.domain.repository

import com.fiction.entities.roommodels.OnBoardingSettingsEntity

interface OnBoardingSettingRepo {
    suspend fun setOnBoardingSettingsData(onBoardingSettingsEntity: OnBoardingSettingsEntity)
    suspend fun getOnBoardingSettingsData(): OnBoardingSettingsEntity?
    suspend fun deleteTable()
}
