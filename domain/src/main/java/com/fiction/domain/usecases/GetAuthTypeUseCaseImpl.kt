package com.fiction.domain.usecases

import com.fiction.domain.interactors.GetAuthTypeUseCase
import com.fiction.domain.model.enums.AuthType
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.first

class GetAuthTypeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
) : GetAuthTypeUseCase {

    override suspend fun invoke(): AuthType {
        val value = dataStoreRepository.getAuthType().first()
        return value?.let {
            if (it.isEmpty()) AuthType.GUEST
            else AuthType.valueOf(it)
        } ?: AuthType.GUEST
    }
}