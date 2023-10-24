package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class SearchBookWithNameResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("title")
    val bookName: String?,
    @SerializedName("description")
    val contextText: String?,
    @SerializedName("covers")
    val imageLink: CoversResponse?,
    @SerializedName("genres")
    val genres: List<GenreResponse>?
)
