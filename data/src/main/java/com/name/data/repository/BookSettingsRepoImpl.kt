package com.name.data.repository

import com.name.data.dataservice.sqlservice.BookSettingsDao
import com.name.domain.repository.BookSettingsRepo
import com.name.entities.roommodels.BookSettingsEntity

class BookSettingsRepoImpl(private val bookSettingsDao: BookSettingsDao) : BookSettingsRepo {

    override suspend fun setBookSettingsData(bookSettingsEntity: BookSettingsEntity) {
        bookSettingsDao.insertData(bookSettingsEntity)
    }

    override suspend fun getBookSettingsData(): BookSettingsEntity {
        return bookSettingsDao.getItem()
    }

}