package com.fiction.entities.response.updatedevice

import android.os.Build
import com.google.gson.annotations.SerializedName

data class UpdateDeviceRequest(
    @SerializedName("platform")
    val platform: String = "android",
    @SerializedName("platformVersion")
    val platformVersion: String = Build.VERSION.RELEASE,
    @SerializedName("appVersion")
    val appVersion: String,
    @SerializedName("deviceName")
    val deviceName: String = Build.MODEL,
    @SerializedName("buildVersion")
    val buildVersion: String,
    @SerializedName("udid")
    val udid: String,
    @SerializedName("timezone")
    val timezone: String,
)
