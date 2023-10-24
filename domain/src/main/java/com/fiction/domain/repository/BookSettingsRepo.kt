package com.fiction.domain.repository

import com.fiction.entities.roommodels.BookSettingsEntity

interface BookSettingsRepo {
    suspend fun setBookSettingsData(bookSettingsEntity: BookSettingsEntity)
    suspend fun getBookSettingsData():BookSettingsEntity?
}