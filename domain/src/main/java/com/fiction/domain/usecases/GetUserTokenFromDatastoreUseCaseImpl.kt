package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetUserTokenFromDatastoreUseCase
import com.fiction.domain.repository.DataStoreRepository

class GetUserTokenFromDatastoreUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : GetUserTokenFromDatastoreUseCase {

    override suspend fun invoke() = dataStoreRepository.getUserToken()

}