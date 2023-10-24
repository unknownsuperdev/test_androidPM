package com.fiction.domain.usecases

import com.fiction.domain.interactors.SetIsAppLaunchFirstTimeUseCase
import com.fiction.domain.repository.DataStoreRepository

class SetIsAppLaunchFirstTimeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : SetIsAppLaunchFirstTimeUseCase {

    override suspend fun invoke(isOpened: Boolean) {
        dataStoreRepository.setIsAppLaunchFirstTime(isOpened)
    }
}