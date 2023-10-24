package com.fiction.data.repository

import com.fiction.data.dataservice.appservice.PreferencesService
import com.fiction.domain.model.registration.Token
import com.fiction.domain.repository.DataStoreRepository
import kotlinx.coroutines.flow.Flow

class DataStoreRepositoryImpl(private val dataStoreService: PreferencesService) :
    DataStoreRepository {

    override suspend fun setToken(token: Token?) {
        dataStoreService.setToken(token?.accessToken ?: "")
    }

    override suspend fun setIsLogged(isLogged: Boolean) {
        dataStoreService.setIsLogged(isLogged)
    }

    override suspend fun getIsLogged(): Boolean? =
        dataStoreService.getIsLogged()

    override suspend fun setRefreshToken(token: String) {
        dataStoreService.setRefreshToken(token)
    }

    override suspend fun getUserToken() = dataStoreService.getToken()

    override suspend fun setIsAppLaunchFirstTime(isOpened: Boolean) {
        dataStoreService.setIsAppLaunchFirstTime(isOpened)
    }

    override suspend fun getIsAppLaunchFirstTime(): Flow<Boolean?> =
        dataStoreService.getIsAppLaunchFirstTime()

    override suspend fun setIsDataRestored(isRestored: Boolean) {
        dataStoreService.setIsDataRestored(isRestored)
    }

    override suspend fun getIsDataRestored(): Flow<Boolean?> =
        dataStoreService.getIsDataRestored()

    override suspend fun setPushToken(pushToken: String) {
        dataStoreService.setPushToken(pushToken)
    }

    override suspend fun getPushToken(): Flow<String?> =
        dataStoreService.getPushToken()

    override suspend fun setIsGetGuestNewTokenState(isGetNewToken: Boolean) {
        dataStoreService.setIsGetGuestNewTokenState(isGetNewToken)
    }

    override suspend fun checkIsGetGuestNewToken(): Flow<Boolean?> =
        dataStoreService.checkIsGetGuestNewToken()

    override suspend fun storeUuid(uuid: String) {
        dataStoreService.storeUuid(uuid)
    }

    override suspend fun getUuid(): Flow<String?> =
        dataStoreService.getUuid()

    override suspend fun setIsFirstPurchase(isFirst: Boolean){
        dataStoreService.setIsFirstPurchase(isFirst)
    }

    override suspend fun getIsFirstPurchase(): Flow<Boolean?> =
        dataStoreService.getIsFirstPurchase()

    override suspend fun setUserId(userId: String) {
        dataStoreService.setUserId(userId)
    }

    override suspend fun getUserId(): Flow<String?> =
        dataStoreService.getUserId()

    override suspend fun setGaid(gaid: String) {
        dataStoreService.setGaid(gaid)
    }
    override suspend fun getGaid(): Flow<String?> =
        dataStoreService.getGaid()

    override suspend fun setAuthType(type: String) {
        dataStoreService.setAuthType(type)
    }

    override suspend fun getAuthType(): Flow<String?> =
        dataStoreService.getAuthType()

    override suspend fun setIsExploreFirstTime(hasShow: Boolean) {
        dataStoreService.setIsExploreFirstTime(hasShow)
    }

    override suspend fun getIsExploreFirstTime(): Flow<Boolean?> {
       return dataStoreService.getIsExploreFirstTime()
    }

}
