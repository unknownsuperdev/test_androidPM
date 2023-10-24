package com.name.entities.responce.explore


import com.google.gson.annotations.SerializedName

data class ChapterResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_free")
    val isFree: Int?,
    @SerializedName("title")
    val title: String?
)