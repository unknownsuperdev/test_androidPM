package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetPushTokenFromLocalUseCase
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetPushTokenFromLocalUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
): GetPushTokenFromLocalUseCase {

    override suspend fun invoke(): Flow<String?> =
        dataStoreRepository.getPushToken()

}