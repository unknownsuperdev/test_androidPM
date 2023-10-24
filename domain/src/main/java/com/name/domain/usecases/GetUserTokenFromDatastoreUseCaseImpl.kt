package com.name.domain.usecases

import com.name.domain.interactors.GetUserTokenFromDatastoreUseCase
import com.name.domain.repository.DataStoreRepository

class GetUserTokenFromDatastoreUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : GetUserTokenFromDatastoreUseCase {

    override suspend fun invoke() = dataStoreRepository.getUserToken()

}