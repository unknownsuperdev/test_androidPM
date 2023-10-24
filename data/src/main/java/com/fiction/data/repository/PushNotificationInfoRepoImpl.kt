package com.fiction.data.repository

import com.fiction.data.dataservice.sqlservice.PushNotificationInfoDao
import com.fiction.domain.repository.PushNotificationInfoRepo
import com.fiction.entities.roommodels.PushNotificationInfoEntity

class PushNotificationInfoRepoImpl(
    private val pushNotificationInfoDao: PushNotificationInfoDao
): PushNotificationInfoRepo {

    override suspend fun insertData(item: PushNotificationInfoEntity) {
        pushNotificationInfoDao.insertData(item)
    }

    override suspend fun getNotificationSize(): Int? =
        pushNotificationInfoDao.getNotificationSize()

    override suspend fun deleteNotId(notId: Int) {
        pushNotificationInfoDao.deleteNotId(notId)
    }

    override suspend fun getLastAddedNotId(): Int? =
        pushNotificationInfoDao.getLastAddedNoteId()


    override suspend fun getFirstAddedNotId(): Int? =
        pushNotificationInfoDao.getFirstAddedNoteId()

}