package com.fiction.entities.response.registration.forgotpassword

import com.google.gson.annotations.SerializedName

data class MessageResponse(
    @SerializedName("message")
    val message: String?
)
