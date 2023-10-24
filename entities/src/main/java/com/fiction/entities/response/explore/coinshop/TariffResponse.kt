package com.fiction.entities.response.explore.coinshop

import com.google.gson.annotations.SerializedName

data class TariffResponse(
    @SerializedName("coins")
    val coins: Int?,
    @SerializedName("content")
    val contentResponse: ContentResponse,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("is_paid")
    val isPaid: Boolean?,
    @SerializedName("item")
    val item: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("theme")
    val themeResponse: ThemeResponse?
)