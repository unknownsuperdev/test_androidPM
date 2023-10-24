package com.name.entities.responce.registration

import com.google.gson.annotations.SerializedName

data class GuestTokenResponse(
    @SerializedName("access_token")
    val accessToken: String
)
