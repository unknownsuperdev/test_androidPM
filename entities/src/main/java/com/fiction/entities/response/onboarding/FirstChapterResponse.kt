package com.fiction.entities.response.onboarding


import com.google.gson.annotations.SerializedName

data class FirstChapterResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("text")
    val text: String?,
    @SerializedName("title")
    val title: String?
)
