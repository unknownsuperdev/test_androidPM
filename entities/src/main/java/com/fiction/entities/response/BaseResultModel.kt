package com.fiction.entities.response


import com.google.gson.annotations.SerializedName

data class BaseResultModel<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("success")
    val success: Boolean,
)

data class BaseErrorResultModel(
    @SerializedName("data")
    val data: Any?,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("errors")
    val errors: List<Errors>,
    @SerializedName("code")
    val code: Int,
    @SerializedName("status")
    val status: String
) {
    data class Errors(
        @SerializedName("name")
        val name: String,
        @SerializedName("messages")
        val messages: List<String>
    )
}