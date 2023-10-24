package com.fiction.entities.request.analytic


import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class AnalyticGoogleRequest(
    @SerializedName("udid")
    val udid: String?,
    @SerializedName("webinfo")
    val webinfo: JSONObject?
)