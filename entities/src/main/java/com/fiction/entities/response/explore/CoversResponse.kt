package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class CoversResponse(
    @SerializedName("img_cover")
    val imgCover: String?,
    @SerializedName("img_horizontal")
    val imgHorizontal: String?,
    @SerializedName("img_summary")
    val imgSummary: String?
)