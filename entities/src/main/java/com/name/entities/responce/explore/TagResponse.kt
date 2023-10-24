package com.name.entities.responce.explore


import com.google.gson.annotations.SerializedName

data class TagResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?
)