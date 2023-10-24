package com.fiction.entities.response.explore.coinshop

import com.google.gson.annotations.SerializedName

data class ContentResponse(
    @SerializedName("description")
    val description: String?,
    @SerializedName("sub_title")
    val discountPercent: String?,
    @SerializedName("sup_title")
    val additionalCoins: String?,
    @SerializedName("title")
    val title: String?
)