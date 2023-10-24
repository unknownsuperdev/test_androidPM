package com.fiction.data.dataservice.appservice

import android.content.Context
import com.fiction.data.util.Constants.Companion.DEFAULT_BOOLEAN
import com.fiction.data.util.Constants.Companion.DEFAULT_FLOAT
import com.fiction.data.util.Constants.Companion.DEFAULT_INT
import com.fiction.data.util.Constants.Companion.DEFAULT_LONG
import com.fiction.data.util.Constants.Companion.EMPTY_STRING
import com.fiction.data.util.emitFlow
import kotlinx.coroutines.flow.Flow

class PreferencesServiceImpl(context: Context) : PreferencesService {

    private val sharedPrefs = context.getSharedPreferences("info", Context.MODE_PRIVATE)
    private val editor = sharedPrefs.edit()

    private val userAuthenticationKey = "userAuthenticationKey"
    private val isLoggedKey = "isLoggedKey"
    private val refreshAuthenticationKey = "refreshAuthenticationKey"
    private val appLunchKey = "appLunchKey"
    private val restoreKey = "restoreKey"
    private val pushTokenKey = "pushTokenKey"
    private val isGetGuestNewToken = "isGetGuestNewToken"
    private val storeUuid = "storeUuid"
    private val isFirstPurchase = "isFirstPurchase"
    private val userId = "userId"
    private val gaidKey = "gaid"
    private val hasFirstShowExplore = "hasFirstShowExplore"
    private val authTypeKey = "authTypeKey"

    private fun set(key: String, value: Any?) {
        when (value) {
            is Int -> editor.putInt(key, value).apply()
            is Long -> editor.putLong(key, value).apply()
            is Float -> editor.putFloat(key, value).apply()
            is String -> editor.putString(key, value).apply()
            is Boolean -> editor.putBoolean(key, value).apply()
            else -> throw UnsupportedOperationException("Not implemented type")
        }
    }

    private inline operator fun <reified T> get(
        key: String
    ): T {
        return when (T::class) {
            Int::class -> sharedPrefs.getInt(key, DEFAULT_INT) as T
            Long::class -> sharedPrefs.getLong(key, DEFAULT_LONG) as T
            Float::class -> sharedPrefs.getFloat(key, DEFAULT_FLOAT) as T
            String::class -> sharedPrefs.getString(key, EMPTY_STRING).orEmpty() as T
            Boolean::class -> sharedPrefs.getBoolean(key, DEFAULT_BOOLEAN) as T
            else -> throw UnsupportedOperationException("Not implemented type")
        }
    }

    override suspend fun setIsLogged(data: Boolean) {
        set(isLoggedKey, data)
    }

    override fun getIsLogged(): Boolean =
        get(isLoggedKey)


    override suspend fun setToken(token: String) {
        set(userAuthenticationKey, token)
    }

    override fun getToken(): String? = get(userAuthenticationKey)

    override suspend fun setRefreshToken(token: String) {
        set(refreshAuthenticationKey, token)
    }

    override fun getRefreshToken(): String? = get(refreshAuthenticationKey)

    override suspend fun setIsAppLaunchFirstTime(isOpened: Boolean) {
        set(appLunchKey, isOpened)
    }

    override fun getIsAppLaunchFirstTime(): Flow<Boolean?> = emitFlow {
        get(appLunchKey)
    }

    override suspend fun setIsDataRestored(isRestored: Boolean) {
        set(restoreKey, isRestored)
    }

    override fun getIsDataRestored(): Flow<Boolean?> = emitFlow {
        get(restoreKey)
    }

    override suspend fun setPushToken(pushToken: String) {
        set(pushTokenKey, pushToken)
    }

    override fun getPushToken(): Flow<String?> = emitFlow {
        get(pushTokenKey)
    }

    override suspend fun setIsGetGuestNewTokenState(isGetNewToken: Boolean) {
        set(isGetGuestNewToken, isGetNewToken)
    }

    override fun checkIsGetGuestNewToken(): Flow<Boolean?> = emitFlow {
        get(isGetGuestNewToken)
    }

    override suspend fun storeUuid(uuid: String) {
        set(storeUuid, uuid)
    }

    override fun getUuid(): Flow<String?> =
        emitFlow {
            get(storeUuid)
        }

    override suspend fun setIsFirstPurchase(isFirst: Boolean) {
        set(isFirstPurchase, isFirst)
    }

    override fun getIsFirstPurchase(): Flow<Boolean?> =
        emitFlow {
            get(isFirstPurchase)
        }

    override suspend fun setUserId(userId: String) {
        set(userId, userId)
    }

    override fun getUserId(): Flow<String?> =
        emitFlow {
            get(userId)
        }

    override suspend fun setGaid(gaid: String) {
        set(gaidKey, gaid)
    }

    override fun getGaid(): Flow<String?> =
        emitFlow {
            get(gaidKey)
        }

    override suspend fun setAuthType(type: String)  {
        set(authTypeKey, type)
    }
    override suspend fun setIsExploreFirstTime(hasShow: Boolean) {
        set(hasFirstShowExplore, hasShow)
    }

    override fun getIsExploreFirstTime(): Flow<Boolean?> =
        emitFlow {
            get(hasFirstShowExplore)
        }

    override fun getAuthType(): Flow<String?>  =
        emitFlow {
            get(authTypeKey)
        }
}
