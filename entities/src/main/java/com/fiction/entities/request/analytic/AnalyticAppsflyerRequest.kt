package com.fiction.entities.request.analytic


import com.google.gson.annotations.SerializedName

data class AnalyticAppsflyerRequest(
    @SerializedName("appsflyer_id")
    val appsflyerId: String?,
    @SerializedName("idfa")
    val idfa: String?,
    @SerializedName("unique_id")
    val uniqueId: String?
)