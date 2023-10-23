package ru.tripster.data.dataservice.appservice

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

class DataStoreServiceImpl(private val context: Context) : DataStoreService {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "info")
    private val result = context.dataStore
    private val userAuthenticationKey = "userAuthenticationKey"
    private val guestAuthenticationKey = "guestAuthenticationKey"
    private val deviceIdKey = "deviceIdKey"
    private val saveExperienceIdFiltrationKey = "saveExperienceIdFiltrationKey"
    private val saveExperienceTitleFiltrationKey = "saveExperienceTitleFiltrationKey"
    private val saveClosingTimeIdFiltrationKey = "saveClosingTimeIdFiltrationKey"
    private val saveUserData = "saveUserData"
    private val saveGuidEmail = "saveGuidEmail"
    private val saveGuidId = "saveGuidId"
    private val saveMenuItem = "saveMenuItem"
    private val saveCurrentStage = "saveCurrentStage"
    private val saveLastRemindLaterTime = "saveLastRemindLaterTime"
    private val saveNotificationState = "saveNotificationState"

    private suspend fun set(key: String, value: Any?) {
        when (value) {
            is Int -> result.edit { edit ->
                val tokenKey = preferencesKey<Int>(key)
                edit[tokenKey] = value
            }

            is Long -> result.edit { edit ->
                val tokenKey = preferencesKey<Long>(key)
                edit[tokenKey] = value
            }

            is Float -> result.edit { edit ->
                val tokenKey = preferencesKey<Float>(key)
                edit[tokenKey] = value
            }

            is String -> result.edit { edit ->
                val tokenKey = preferencesKey<String>(key)
                edit[tokenKey] = value
            }

            is Boolean -> result.edit { edit ->
                val tokenKey = preferencesKey<Boolean>(key)
                edit[tokenKey] = value
            }

            else -> throw UnsupportedOperationException("Not implemented type")
        }
    }

    private inline operator fun <reified T> get(
        key: String
    ): Flow<T> {
        val preferencesKey = intPreferencesKey(key)
        return when (T::class) {
            Int::class ->
                result.data.map { preferences ->
                    preferences[preferencesKey<Int>(key)] ?: -1
                } as Flow<T>

            Long::class ->
                result.data.map { preferences ->
                    preferences[preferencesKey<Long>(key)] ?: -1L
                } as Flow<T>

            Float::class ->
                result.data.map { preferences ->
                    preferences[preferencesKey<Float>(key)] ?: -1f
                } as Flow<T>

            String::class ->
                result.data.map { preferences ->
                    preferences[preferencesKey<String>(key)] ?: ""
                } as Flow<T>

            Boolean::class ->
                result.data.map { preferences ->
                    preferences[preferencesKey<Boolean>(key)] ?: false
                } as Flow<T>

            else -> throw UnsupportedOperationException("Not implemented type")
        }
    }

    private inline fun <reified T> preferencesKey(key: String): Preferences.Key<T> {
        return when (T::class) {
            Int::class -> intPreferencesKey(key) as Preferences.Key<T>
            Long::class -> longPreferencesKey(key) as Preferences.Key<T>
            Float::class -> floatPreferencesKey(key) as Preferences.Key<T>
            String::class -> stringPreferencesKey(key) as Preferences.Key<T>
            Boolean::class -> booleanPreferencesKey(key) as Preferences.Key<T>
            else -> throw UnsupportedOperationException("Preferences key type is not found")
        }
    }

    override suspend fun setGuestToken(token: String) {
        set(guestAuthenticationKey, token)
    }

    override fun getGuestToken(): Flow<String?> = get(guestAuthenticationKey)
    override suspend fun setUserData(emailAndPassword: String?) {
        set(saveUserData, emailAndPassword)
    }

    override fun getUserData(): Flow<String?> = get(saveUserData)
    override suspend fun saveGuidId(guidId: Int) {
        set(saveGuidId, guidId)
    }

    override fun getGuidId(): Flow<Int?> = get(saveGuidId)

    override suspend fun saveGuidEmail(guidEmail: String) {
        set(saveGuidEmail, guidEmail)
    }

    override fun getGuidEmail(): Flow<String?> = get(saveGuidEmail)
    override suspend fun saveMenuItem(item: String?) {
        set(saveMenuItem, item)
    }

    override fun getMenuItem(): Flow<String?> = get(saveMenuItem)
    override suspend fun saveCurrentStage(stage: String?) {
        set(saveCurrentStage, stage)
    }

    override fun getCurrentStage(): Flow<String?> = get(saveCurrentStage)
    override suspend fun saveLastRemindLaterTime(time: String?) {
        set(saveLastRemindLaterTime, time)
    }

    override fun getLastRemindLaterTime(): Flow<String?> = get(saveLastRemindLaterTime)
    override suspend fun saveNotificationState(isSelected: Boolean?) {
        set(saveNotificationState, isSelected)
    }

    override fun getNotificationState(): Flow<Boolean?> = get(saveNotificationState)

    override suspend fun setUserToken(token: String) {
        set(userAuthenticationKey, token)
    }

    override fun getUserToken(): Flow<String?> = get(userAuthenticationKey)
    override suspend fun saveExperienceIdFiltration(experienceId: Int) {
        set(saveExperienceIdFiltrationKey, experienceId)
    }

    override fun getExperienceIdFiltration(): Flow<Int?> = get(saveExperienceIdFiltrationKey)
    override suspend fun saveExperienceTitleFiltration(title: String) {
        set(saveExperienceTitleFiltrationKey, title)
    }

    override fun getExperienceTitleFiltration(): Flow<String?> =
        get(saveExperienceTitleFiltrationKey)

    override suspend fun saveClosingTimeFiltrationId(experienceId: Int?) {
        set(saveClosingTimeIdFiltrationKey, experienceId)
    }

    override fun getClosingTimeFiltrationId(): Flow<Int?> = get(saveClosingTimeIdFiltrationKey)

    override suspend fun setDeviceID(id: String) {
        set(deviceIdKey, id)
    }

    override fun getDeviceId(): Flow<String?> = get(deviceIdKey)
}