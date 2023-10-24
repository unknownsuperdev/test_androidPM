package com.fiction.entities.response.analytic

import com.google.gson.annotations.SerializedName

data class AnalyticResponse(
    @SerializedName("status")
    val status: String?
)
