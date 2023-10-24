package com.fiction.entities.request.analytic


import com.google.gson.annotations.SerializedName
import org.json.JSONObject

data class AnalyticTiktokRequest(
    @SerializedName("webinfo")
    val webinfo: JSONObject?
)