package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetIsLoggedStatusUseCase
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class GetIsLoggedStatusUseCaseImpl(
    private val dataStoreRepo: DataStoreRepository
): GetIsLoggedStatusUseCase {

    override suspend fun invoke(): Boolean? =
        dataStoreRepo.getIsLogged()
}