package com.name.domain.repository

import com.name.domain.model.registration.GuestToken
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun setGuestToken(token: GuestToken)
    suspend fun getUserToken(): Flow<String?>

    suspend fun setIsOpenedWelcomeScreen(isOpened: Boolean)

    suspend fun getIsOpenedWelcomeScreen(): Flow<Boolean?>
}