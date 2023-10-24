package com.fiction.data.repository

import com.fiction.data.dataservice.sqlservice.LaunchInfoDao
import com.fiction.domain.repository.LaunchInfoRepo
import com.fiction.entities.roommodels.LaunchInfoEntity
import com.fiction.entities.utils.Constants

class LaunchInfoRepoImpl(
    private val launchInfoDao: LaunchInfoDao
): LaunchInfoRepo {

    override suspend fun insertData(item: LaunchInfoEntity) {
        launchInfoDao.insertData(item)
    }

    override suspend fun getIsInstall(id: Int): Boolean? =
        launchInfoDao.getIsInstall(id)
}