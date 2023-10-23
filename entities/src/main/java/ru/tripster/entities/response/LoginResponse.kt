package ru.tripster.entities.response

import com.google.gson.annotations.*

data class LoginResponse(
    @SerializedName("token")
    val token:String?
)
