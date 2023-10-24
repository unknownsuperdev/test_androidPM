package com.fiction.entities.response.registration.forgotpassword


import com.google.gson.annotations.SerializedName

data class ForgotPasswordResponse(
    @SerializedName("code")
    val code: Int?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("message")
    val message: String?
)