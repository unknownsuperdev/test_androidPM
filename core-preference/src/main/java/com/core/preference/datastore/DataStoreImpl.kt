package com.core.preference.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.core.preference.PreferenceStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class DataStoreImpl(private val context: Context, namePreference: String, needMigration: Boolean) :
    PreferenceStore {

    private val Context.dataStore: DataStore<Preferences> by if (needMigration) preferencesDataStore(
        name = namePreference,
        produceMigrations = { context ->
            listOf(SharedPreferencesMigration(context, namePreference))
        }
    ) else preferencesDataStore(name = namePreference)

    @Suppress("UNCHECKED_CAST")
    override suspend fun setValue(key: String, value: Any?) {
        when (value) {
            is Int -> context.dataStore.edit { edit ->
                val preferencesKey = preferencesKey<Int>(key)
                edit[preferencesKey] = value
            }
            is Long -> context.dataStore.edit { edit ->
                val preferencesKey = preferencesKey<Long>(key)
                edit[preferencesKey] = value
            }
            is Float -> context.dataStore.edit { edit ->
                val preferencesKey = preferencesKey<Float>(key)
                edit[preferencesKey] = value
            }
            is String -> context.dataStore.edit { edit ->
                val preferencesKey = preferencesKey<String>(key)
                edit[preferencesKey] = value
            }
            is Boolean -> context.dataStore.edit { edit ->
                val preferencesKey = preferencesKey<Boolean>(key)
                edit[preferencesKey] = value
            }
            is Set<*> -> context.dataStore.edit { edit ->
                val preferencesKey = preferencesKey<Set<String>>(key)
                edit[preferencesKey] = value as Set<String>
            }
            else -> throw UnsupportedOperationException("Not implemented type")
        }
    }

    override fun getLong(key: String): Flow<Long?> {
        return context.dataStore.data.map {
            it[preferencesKey<Long>(key)]
        }
    }

    override fun getBoolean(key: String): Flow<Boolean?> {
        return context.dataStore.data.map {
            it[preferencesKey<Boolean>(key)]
        }
    }

    override fun getString(key: String): Flow<String?> {
        return context.dataStore.data.map {
            it[preferencesKey<String>(key)]
        }
    }

    override fun getInt(key: String): Flow<Int?> {
        return context.dataStore.data.map {
            it[preferencesKey<Int>(key)]
        }
    }

    override fun getFloat(key: String): Flow<Float?> {
        return context.dataStore.data.map {
            it[preferencesKey<Float>(key)]
        }
    }

    override fun getSetString(key: String): Flow<Set<String>?> {
        return context.dataStore.data.map {
            it[preferencesKey<Set<String>>(key)]
        }
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> preferencesKey(key: String): Preferences.Key<T> {
        return when (T::class) {
            Int::class -> intPreferencesKey(key) as Preferences.Key<T>
            Long::class -> longPreferencesKey(key) as Preferences.Key<T>
            Float::class -> floatPreferencesKey(key) as Preferences.Key<T>
            String::class -> stringPreferencesKey(key) as Preferences.Key<T>
            Boolean::class -> booleanPreferencesKey(key) as Preferences.Key<T>
            Set::class -> stringSetPreferencesKey(key) as Preferences.Key<T>
            else -> throw UnsupportedOperationException("Preferences key type is not found")
        }
    }

    override suspend fun clear() {
        context.dataStore.edit {
            it.clear()
        }
    }
}