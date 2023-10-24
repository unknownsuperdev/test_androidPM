package com.name.domain.repository

import com.name.entities.roommodels.BookSettingsEntity
import kotlinx.coroutines.flow.Flow

interface BookSettingsRepo {
    suspend fun setBookSettingsData(bookSettingsEntity: BookSettingsEntity)
    suspend fun getBookSettingsData():BookSettingsEntity?
}