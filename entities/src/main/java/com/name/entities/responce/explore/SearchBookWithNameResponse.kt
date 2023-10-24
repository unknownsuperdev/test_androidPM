package com.name.entities.responce.explore

import com.google.gson.annotations.SerializedName

data class SearchBookWithNameResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val bookName: String = "",
    @SerializedName("description")
    val contextText: String = "",
    @SerializedName("cover")
    val imageLink: String = ""
)
