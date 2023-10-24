package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class ReadProgress(
    @SerializedName("percent")
    val percent: Double
)
