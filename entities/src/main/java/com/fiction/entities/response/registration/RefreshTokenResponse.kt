package com.fiction.entities.response.registration

import com.google.gson.annotations.SerializedName

data class RefreshTokenResponse(
    @SerializedName("data")
    val data: TokenResponse?
)