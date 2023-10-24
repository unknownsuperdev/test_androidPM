package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class ReadingProgressResponse(
    @SerializedName("chapter_id")
    val chapterId: Long?,
    @SerializedName("percent")
    val percent: Double?
)