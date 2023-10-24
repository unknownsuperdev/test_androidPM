package com.fiction.domain.repository

import com.fiction.entities.roommodels.PushNotificationInfoEntity

interface PushNotificationInfoRepo {
    suspend fun insertData(item: PushNotificationInfoEntity)
    suspend fun getNotificationSize(): Int?
    suspend fun deleteNotId(notId: Int)
    suspend fun getLastAddedNotId(): Int?
    suspend fun getFirstAddedNotId(): Int?
}