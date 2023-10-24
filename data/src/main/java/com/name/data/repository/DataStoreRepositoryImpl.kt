package com.name.data.repository

import com.name.data.dataservice.appservice.DataStoreService
import com.name.domain.model.registration.GuestToken
import com.name.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class DataStoreRepositoryImpl(private val dataStoreService: DataStoreService) :
    DataStoreRepository {

    override suspend fun setGuestToken(token: GuestToken) {
        dataStoreService.setGuestToken(token.accessToken)
    }

    override suspend fun getUserToken() = dataStoreService.getUserToken()

    override suspend fun setIsOpenedWelcomeScreen(isOpened: Boolean) {
        dataStoreService.setIsWelcomeScreenOpened(isOpened)
    }

    override suspend fun getIsOpenedWelcomeScreen(): Flow<Boolean?> =
        dataStoreService.getIsWelcomeScreenOpened()

}