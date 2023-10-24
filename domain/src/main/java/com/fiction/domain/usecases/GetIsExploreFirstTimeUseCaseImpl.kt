package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetIsExploreFirstTimeUseCase
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsExploreFirstTimeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : GetIsExploreFirstTimeUseCase {

    override suspend fun invoke(): Flow<Boolean?> {
        return dataStoreRepository.getIsExploreFirstTime()
    }
}