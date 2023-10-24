package com.fiction.entities.response.gifts


import com.google.gson.annotations.SerializedName

data class GiftBodyResponse(
    @SerializedName("chapters")
    val chapters: Int?,
    @SerializedName("coins")
    val coins: Int?,
    @SerializedName("content")
    val content: String?,
    @SerializedName("title")
    val title: String?
)