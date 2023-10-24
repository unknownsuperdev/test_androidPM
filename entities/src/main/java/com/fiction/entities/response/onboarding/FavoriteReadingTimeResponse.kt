package com.fiction.entities.response.onboarding


import com.google.gson.annotations.SerializedName

data class FavoriteReadingTimeResponse(
    @SerializedName("id")
    val id: Int?,
    @SerializedName("name")
    val name: String?
)
