package com.fiction.domain.repository

import com.fiction.entities.roommodels.LaunchInfoEntity

interface LaunchInfoRepo {
    suspend fun insertData(item: LaunchInfoEntity)
    suspend fun getIsInstall(id: Int) : Boolean?
}