package com.fiction.entities.response.explore.coinshop

import com.google.gson.annotations.SerializedName

data class ThemeResponse(
    @SerializedName("light")
    val lightResponse: LightResponse?
)