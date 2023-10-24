package com.name.entities.responce.explore

import com.google.gson.annotations.SerializedName

data class SearchBookWithTagResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val tagName: String?
)
