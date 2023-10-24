package com.fiction.entities.request.launch

import android.os.Build
import com.fiction.entities.BuildConfig
import com.google.gson.annotations.SerializedName

data class LaunchRequest(
    @SerializedName("platform")
    val platform: String = "android",
    @SerializedName("platformVersion")
    val platformVersion: String = Build.VERSION.RELEASE,
    @SerializedName("appVersion")
    val appVersion: String = BuildConfig.VERSION_NAME,
    @SerializedName("deviceName")
    val deviceName: String = Build.MODEL,
    @SerializedName("udid")
    val udid: String,
    @SerializedName("is_install")
    val isInstall: Boolean
)
