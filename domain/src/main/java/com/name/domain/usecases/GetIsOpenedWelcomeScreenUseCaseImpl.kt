package com.name.domain.usecases

import com.name.domain.interactors.GetIsOpenedWelcomeScreenUseCase
import com.name.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsOpenedWelcomeScreenUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
): GetIsOpenedWelcomeScreenUseCase {

    override suspend fun invoke(): Flow<Boolean?> =
        dataStoreRepository.getIsOpenedWelcomeScreen()

}