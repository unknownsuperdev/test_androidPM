package com.fiction.entities.response.gifts

import com.google.gson.annotations.SerializedName

data class GiftExistResponse(
    @SerializedName("exist")
    val exist: Boolean
)
