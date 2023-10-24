package com.fiction.entities.request.analytic


import com.google.gson.annotations.SerializedName

data class AnalyticAdjustRequest(
    @SerializedName("adid")
    val adid: String?,
    @SerializedName("bundle_id")
    val bundleId: String?,
    @SerializedName("idfa")
    val idfa: String?,
    @SerializedName("udid")
    val udid: String?
)