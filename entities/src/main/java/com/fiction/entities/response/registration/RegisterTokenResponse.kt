package com.fiction.entities.response.registration

import com.google.gson.annotations.SerializedName


data class RegisterTokenResponse(
    @SerializedName("access_token")
    val accessToken: String
)
