package com.name.data.dataservice.appservice

import kotlinx.coroutines.flow.*

interface DataStoreService {

    suspend fun setUserToken(token: String)
    fun getUserToken(): Flow<String?>

    suspend fun setGuestToken(token: String)
    fun getGuestToken(): Flow<String?>

}