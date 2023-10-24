package com.fiction.entities.response.explore.coinshop

import com.google.gson.annotations.SerializedName

data class IconResponse(
    @SerializedName("pdf")
    val pdf: String?,
    @SerializedName("svg")
    val svg: String?
)