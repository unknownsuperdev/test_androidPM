package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class GenreResponse(
    @SerializedName("icon")
    val icon: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?
)