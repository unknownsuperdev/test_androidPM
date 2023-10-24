package com.fiction.entities.response.gifts

import com.google.gson.annotations.SerializedName

data class CompleteGiftResponse(
    @SerializedName("message")
    val message: String
)