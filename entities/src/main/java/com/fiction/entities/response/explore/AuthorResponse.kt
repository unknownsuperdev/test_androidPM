package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class AuthorResponse(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("info")
    val info: String?,
    @SerializedName("pen_name")
    val penName: String?
)