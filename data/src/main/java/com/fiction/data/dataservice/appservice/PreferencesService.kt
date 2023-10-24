package com.fiction.data.dataservice.appservice

import kotlinx.coroutines.flow.Flow

interface PreferencesService {

    suspend fun setToken(token: String)
    fun getToken(): String?

    suspend fun setIsLogged(data: Boolean)
    fun getIsLogged(): Boolean

    suspend fun setRefreshToken(token: String)
    fun getRefreshToken(): String?

    suspend fun setIsAppLaunchFirstTime(isOpened: Boolean)
    fun getIsAppLaunchFirstTime(): Flow<Boolean?>

    suspend fun setIsDataRestored(isRestored: Boolean)
    fun getIsDataRestored(): Flow<Boolean?>

    suspend fun setPushToken(pushToken: String)
    fun getPushToken(): Flow<String?>

    suspend fun setIsGetGuestNewTokenState(isGetNewToken: Boolean)
    fun checkIsGetGuestNewToken(): Flow<Boolean?>

    suspend fun storeUuid(uuid: String)
    fun getUuid(): Flow<String?>

    suspend fun setIsFirstPurchase(isFirst: Boolean)
    fun getIsFirstPurchase(): Flow<Boolean?>

    suspend fun setUserId(userId: String)
    fun getUserId(): Flow<String?>

    suspend fun setGaid(gaid: String)
    fun getGaid(): Flow<String?>
    suspend fun setAuthType(type: String)
    fun getAuthType(): Flow<String?>

    suspend fun setIsExploreFirstTime(hasShow: Boolean)
    fun getIsExploreFirstTime(): Flow<Boolean?>
}
