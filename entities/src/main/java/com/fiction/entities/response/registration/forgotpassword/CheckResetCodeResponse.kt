package com.fiction.entities.response.registration.forgotpassword


import com.google.gson.annotations.SerializedName

data class CheckResetCodeResponse(
    @SerializedName("token")
    val token: String?
)