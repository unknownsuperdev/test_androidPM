package com.fiction.domain.usecases

import com.fiction.domain.interactors.SetIsDataRestoredUseCase
import com.fiction.domain.repository.DataStoreRepository

class SetIsDataRestoredUseCaseImpl(private val dataStoreRepository: DataStoreRepository) :
    SetIsDataRestoredUseCase {
    override suspend fun invoke(isRestored: Boolean) {
        dataStoreRepository.setIsDataRestored(isRestored)
    }

}