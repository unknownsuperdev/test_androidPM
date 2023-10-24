package com.fiction.entities.request.analytic


import com.google.gson.annotations.SerializedName

data class AnalyticAppleRequest(
    @SerializedName("app_version")
    val appVersion: String?,
    @SerializedName("authorization_status")
    val authorizationStatus: String?,
    @SerializedName("os")
    val os: String?,
    @SerializedName("udid")
    val udid: String?
)