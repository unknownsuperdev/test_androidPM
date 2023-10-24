package com.fiction.entities.response.onboarding


import com.google.gson.annotations.SerializedName

data class FavoriteThemeTagResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?
)
