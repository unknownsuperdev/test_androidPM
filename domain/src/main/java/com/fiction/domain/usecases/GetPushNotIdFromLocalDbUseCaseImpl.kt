package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetPushNotIdFromLocalDbUseCase
import com.fiction.domain.repository.PushNotificationInfoRepo
import com.fiction.entities.roommodels.PushNotificationInfoEntity


class GetPushNotIdFromLocalDbUseCaseImpl(
    private val pushNotificationInfoRepo: PushNotificationInfoRepo,
) : GetPushNotIdFromLocalDbUseCase {

    override suspend fun invoke(): Int {
        /*if (pushNotificationInfoRepo.getNotificationSize() == 0)
            pushNotificationInfoRepo.insertData(PushNotificationInfoEntity(0))*/
        val lastId = pushNotificationInfoRepo.getLastAddedNotId() ?: 0
        //pushNotificationInfoRepo.insertData(PushNotificationInfoEntity(lastId.plus(1)))
       return lastId
    }

}