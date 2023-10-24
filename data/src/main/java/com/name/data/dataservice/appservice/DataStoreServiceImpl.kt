package com.name.data.dataservice.appservice

import android.content.*
import androidx.datastore.core.*
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.*

class DataStoreServiceImpl(private val context: Context) : DataStoreService {

	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "info")
	private val result = context.dataStore
	private val userAuthenticationKey = "userAuthenticationKey"
	private val guestAuthenticationKey = "guestAuthenticationKey"
	private val welcomeScreenOpenKey = "welcomeScreenOpenKey"

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

	override suspend fun setGuestToken(token: String) { set(guestAuthenticationKey,token) }
	override fun getGuestToken(): Flow<String?> = get(guestAuthenticationKey)

	override suspend fun setUserToken(token: String) { set(userAuthenticationKey, token) }
	override fun getUserToken(): Flow<String?> = get(userAuthenticationKey)

	override suspend fun setIsWelcomeScreenOpened(isOpened: Boolean) {set(welcomeScreenOpenKey,isOpened)}
	override fun getIsWelcomeScreenOpened(): Flow<Boolean?> = get(welcomeScreenOpenKey)
}