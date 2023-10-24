package com.fiction.entities.response.registration.forgotpassword


import com.google.gson.annotations.SerializedName

data class ResetPasswordRequest(
    @SerializedName("password")
    val password: String,
    @SerializedName("token")
    val token: String
)