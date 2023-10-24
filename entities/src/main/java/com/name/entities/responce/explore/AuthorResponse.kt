package com.name.entities.responce.explore


import com.google.gson.annotations.SerializedName

data class AuthorResponse(
    @SerializedName("avatar")
    val avatar: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("info")
    val info: String?,
    @SerializedName("pen_name")
    val penName: String?
)