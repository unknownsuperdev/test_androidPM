package com.fiction.entities.response.registration.forgotpassword


import com.google.gson.annotations.SerializedName

data class CheckResetCodeRequest(
    @SerializedName("code")
    val code: String,
    @SerializedName("email")
    val email: String
)