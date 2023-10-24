package com.fiction.domain.repository

import com.fiction.domain.model.registration.Token
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {

    suspend fun setToken(token: Token?)
    suspend fun setIsLogged(isLogged: Boolean)
    suspend fun getIsLogged(): Boolean?
    suspend fun getUserToken(): String?

    suspend fun setRefreshToken(token: String)
    suspend fun setIsAppLaunchFirstTime(isOpened: Boolean)
    suspend fun getIsAppLaunchFirstTime(): Flow<Boolean?>
    suspend fun setIsDataRestored(isRestored: Boolean)
    suspend fun getIsDataRestored(): Flow<Boolean?>
    suspend fun setPushToken(pushToken: String)
    suspend fun getPushToken(): Flow<String?>
    suspend fun setIsGetGuestNewTokenState(isGetNewToken: Boolean)
    suspend fun checkIsGetGuestNewToken(): Flow<Boolean?>
    suspend fun storeUuid(uuid: String)
    suspend fun getUuid(): Flow<String?>
    suspend fun setIsFirstPurchase(isFirst: Boolean)
    suspend fun getIsFirstPurchase(): Flow<Boolean?>
    suspend fun setUserId(userId: String)
    suspend fun getUserId(): Flow<String?>
    suspend fun setGaid(gaid: String)
    suspend fun getGaid(): Flow<String?>
    suspend fun setAuthType(type: String)
    suspend fun getAuthType(): Flow<String?>

    suspend fun setIsExploreFirstTime(hasShow: Boolean)
    suspend fun getIsExploreFirstTime(): Flow<Boolean?>
}