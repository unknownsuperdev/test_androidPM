package com.fiction.data.repository

import com.fiction.data.dataservice.sqlservice.OnBoardingSettingsDao
import com.fiction.domain.repository.OnBoardingSettingRepo
import com.fiction.entities.roommodels.OnBoardingSettingsEntity

class OnBoardingSettingRepoImpl(
    private val onBoardingSettingsDao: OnBoardingSettingsDao
): OnBoardingSettingRepo {

    override suspend fun setOnBoardingSettingsData(onBoardingSettingsEntity: OnBoardingSettingsEntity) {
        onBoardingSettingsDao.insertData(onBoardingSettingsEntity)
    }

    override suspend fun getOnBoardingSettingsData(): OnBoardingSettingsEntity? =
        onBoardingSettingsDao.getOnBoardingSettings()

    override suspend fun deleteTable() {
        onBoardingSettingsDao.deleteTable()
    }

}
