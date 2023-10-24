package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetIsAppLaunchFirstTimeUseCase
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsAppLaunchFirstTimeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : GetIsAppLaunchFirstTimeUseCase {

    override suspend fun invoke(): Flow<Boolean?> =
        dataStoreRepository.getIsAppLaunchFirstTime()

}