package com.fiction.entities.response.registration

import com.google.gson.annotations.SerializedName

data class PushToken(
    @SerializedName("push_token")
    val pushToken: String
)
