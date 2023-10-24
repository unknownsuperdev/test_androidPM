package com.fiction.data.repository

import com.fiction.data.dataservice.sqlservice.ImagesDao
import com.fiction.domain.repository.ImagesRepo
import com.fiction.entities.roommodels.ImagesEntity

class ImagesRepoImpl(private val imagesDao: ImagesDao) : ImagesRepo {

    override suspend fun setImage(imagesEntity: ImagesEntity) {
        imagesDao.insertImage(imagesEntity)
    }

    override suspend fun getImage(key: String): ImagesEntity? =
        imagesDao.getImage(key)

}