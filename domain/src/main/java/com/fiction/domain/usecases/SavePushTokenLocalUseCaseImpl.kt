package com.fiction.domain.usecases

import com.fiction.domain.interactors.SavePushTokenLocalUseCase
import com.fiction.domain.repository.DataStoreRepository

class SavePushTokenLocalUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
): SavePushTokenLocalUseCase {

    override suspend fun invoke(pushToken: String) {
        dataStoreRepository.setPushToken(pushToken)
    }
}