package com.analytics.kinesis.data.utils.device

interface DeviceUtilsProvider {

    suspend fun getCountry(): String

    fun getLanguage(): String

    fun getPlatform(): String

    fun getOS(): String

    fun getDeviceUuid(): String

}