package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class ChapterResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("is_free")
    val isFree: Boolean?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("order")
    val order: Int?,
    @SerializedName("coins")
    val coins: Int?,
    @SerializedName("is_purchased")
    val isPurchased: Boolean?,
)