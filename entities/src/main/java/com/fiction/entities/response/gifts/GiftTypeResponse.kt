package com.fiction.entities.response.gifts

import com.google.gson.annotations.SerializedName

enum class GiftTypeResponse {
    @SerializedName("1")
    TYPE_WELCOME_BOX,

    @SerializedName("2")
    TYPE_7_DAYS_CHALLENGE_SUCCESS,

    @SerializedName("3")
    TYPE_7_DAYS_CHALLENGE_FAIL
}