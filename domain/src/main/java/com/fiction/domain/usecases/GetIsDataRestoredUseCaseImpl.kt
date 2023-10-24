package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetIsDataRestoredUseCase
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsDataRestoredUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
) : GetIsDataRestoredUseCase {

    override suspend fun invoke(): Flow<Boolean?> =
        dataStoreRepository.getIsDataRestored()
}