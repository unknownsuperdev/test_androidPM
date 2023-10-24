package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class ChapterInfoResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("coins")
    val coins: Int?,
    @SerializedName("is_purchased")
    val isPurchased: Boolean?,
    @SerializedName("is_published")
    val isPublished: Int?
)