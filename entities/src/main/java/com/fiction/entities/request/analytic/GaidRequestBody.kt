package com.fiction.entities.request.analytic

import com.google.gson.annotations.SerializedName

data class GaidRequestBody(
    @SerializedName("gaid")
    val gaid: String
)
