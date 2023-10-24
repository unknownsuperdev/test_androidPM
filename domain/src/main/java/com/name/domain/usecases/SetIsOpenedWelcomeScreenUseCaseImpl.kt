package com.name.domain.usecases

import com.name.domain.interactors.SetIsOpenedWelcomeScreenUseCase
import com.name.domain.repository.DataStoreRepository

class SetIsOpenedWelcomeScreenUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
): SetIsOpenedWelcomeScreenUseCase {

    override suspend fun invoke(isOpened: Boolean) {
        dataStoreRepository.setIsOpenedWelcomeScreen(isOpened)
    }
}