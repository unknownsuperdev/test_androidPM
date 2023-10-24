package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetUuidUseCase
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetUuidUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
): GetUuidUseCase {

    override suspend fun invoke(): Flow<String?> =
        dataStoreRepository.getUuid()

}