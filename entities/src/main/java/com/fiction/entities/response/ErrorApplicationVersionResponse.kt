package com.fiction.entities.response

import com.google.gson.annotations.SerializedName

data class ErrorApplicationVersionResponse(
    @SerializedName("errors")
    val errors: List<ErrorResponse>?,
    @SerializedName("code")
    val code: String?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("success")
    val success: Boolean?
)

data class ErrorResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("messages")
    val messages: List<String>?
)
