package com.fiction.domain.usecases

import com.fiction.domain.interactors.StoreUuidUseCase
import com.fiction.domain.repository.DataStoreRepository

class StoreUuidUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
): StoreUuidUseCase {

    override suspend fun invoke(uuid: String) {
        dataStoreRepository.storeUuid(uuid)
    }
}