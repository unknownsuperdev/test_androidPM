package com.fiction.domain.usecases

import com.fiction.domain.interactors.SetAuthTypeUseCase
import com.fiction.domain.model.enums.AuthType
import com.fiction.domain.repository.DataStoreRepository

class SetAuthTypeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
): SetAuthTypeUseCase {

    override suspend fun invoke(type: AuthType) {
        dataStoreRepository.setAuthType(type = type.name)
    }
}