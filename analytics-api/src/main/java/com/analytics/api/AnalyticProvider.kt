package com.analytics.api

interface AnalyticProvider {

    fun setUserId(userId: String)

    fun setDeviceId(deviceId: String)

    suspend fun logEvent(event: String, eventProperty: Map<String, Any>)

    suspend fun setUserProperty(userProperty: Map<String, Any>)
}