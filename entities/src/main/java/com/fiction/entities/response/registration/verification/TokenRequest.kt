package com.fiction.entities.response.registration.verification

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("token")
    val token: String?
)