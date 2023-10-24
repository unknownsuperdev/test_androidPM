package com.fiction.domain.usecases

import com.fiction.domain.interactors.SetIsExploreFirstTimeUseCase
import com.fiction.domain.repository.DataStoreRepository

class SetIsExploreFirstTimeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : SetIsExploreFirstTimeUseCase {

    override suspend fun invoke(isFirst: Boolean) {
        dataStoreRepository.setIsExploreFirstTime(isFirst)
    }
}