package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class SearchBookWithTagResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val tagName: String?
)
