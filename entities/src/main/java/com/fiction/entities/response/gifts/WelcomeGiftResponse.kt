package com.fiction.entities.response.gifts


import com.google.gson.annotations.SerializedName

data class WelcomeGiftResponse(
    @SerializedName("body")
    val body: String?,
    @SerializedName("created_at")
    val createdAt: Long?,
    @SerializedName("expired_at")
    val expiredAt: Long?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: GiftTypeResponse?
)