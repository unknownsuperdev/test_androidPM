package com.fiction.entities.response.explore.coinshop

import com.google.gson.annotations.SerializedName

data class LightResponse(
    @SerializedName("background_type")
    val backgroundType: String?,
    @SerializedName("icon")
    val iconResponse: IconResponse?,
    @SerializedName("sup_title_background_color")
    val additionalCoinsBackgroundColor: String?
)