package ru.tripster.entities.request

import com.google.gson.annotations.*

data class LoginRequest(
    @SerializedName("username")
    val username:String,
    @SerializedName("password")
    val password:String,

)
