package com.fiction.domain.repository

import com.fiction.entities.roommodels.ImagesEntity

interface ImagesRepo {
    suspend fun setImage(imagesEntity: ImagesEntity)
    suspend fun getImage(key: String): ImagesEntity?
}