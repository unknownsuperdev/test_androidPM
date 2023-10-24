package com.core.preference

import kotlinx.coroutines.flow.Flow

interface PreferenceStore {

    suspend fun setValue(key: String, value: Any?)

    fun getLong(key: String): Flow<Long?>

    fun getBoolean(key: String): Flow<Boolean?>

    fun getString(key: String): Flow<String?>

    fun getInt(key: String): Flow<Int?>

    fun getFloat(key: String): Flow<Float?>

    fun getSetString(key: String): Flow<Set<String>?>

    suspend fun clear()

}