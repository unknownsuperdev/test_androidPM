package com.fiction.domain.usecases

import com.fiction.domain.interactors.SetIsGetGuestNewTokenStateUseCase
import com.fiction.domain.repository.DataStoreRepository

class SetIsGetGuestNewTokenStateUseCaseImpl(private val dataStoreRepository: DataStoreRepository) :
    SetIsGetGuestNewTokenStateUseCase {
    override suspend fun invoke(isGetNewToken: Boolean) {
        dataStoreRepository.setIsGetGuestNewTokenState(isGetNewToken)
    }
}
