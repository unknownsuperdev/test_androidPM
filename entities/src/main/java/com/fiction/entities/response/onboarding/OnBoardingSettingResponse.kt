package com.fiction.entities.response.onboarding


import com.google.gson.annotations.SerializedName

data class OnBoardingSettingResponse(
    @SerializedName("gender")
    val gender: List<GenderResponse>?,
    @SerializedName("reading_time")
    val readingTime: List<FavoriteReadingTimeResponse>?,
    @SerializedName("screens")
    val screens: List<String>?,
    @SerializedName("tags")
    val tags: List<FavoriteThemeTagResponse>?
)
