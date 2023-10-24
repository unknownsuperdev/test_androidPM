package com.fiction.data.repository

import com.fiction.data.dataservice.sqlservice.BookSettingsDao
import com.fiction.domain.repository.BookSettingsRepo
import com.fiction.entities.roommodels.BookSettingsEntity

class BookSettingsRepoImpl(private val bookSettingsDao: BookSettingsDao) : BookSettingsRepo {

    override suspend fun setBookSettingsData(bookSettingsEntity: BookSettingsEntity) {
        bookSettingsDao.insertData(bookSettingsEntity)
    }

    override suspend fun getBookSettingsData(): BookSettingsEntity {
        return bookSettingsDao.getItem()
    }

}