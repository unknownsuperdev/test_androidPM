package com.fiction.domain.usecases

import com.fiction.domain.interactors.SetPushNotIdToLocalDbUseCase
import com.fiction.domain.repository.PushNotificationInfoRepo
import com.fiction.entities.roommodels.PushNotificationInfoEntity

class SetPushNotIdToLocalDbUseCaseImpl(
    private val pushNotificationInfoRepo: PushNotificationInfoRepo,
): SetPushNotIdToLocalDbUseCase {

    override suspend fun invoke(pushId: Int) {
        pushNotificationInfoRepo.insertData(PushNotificationInfoEntity(pushId))
    }
}