package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class TagBooksResponse(
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("books")
    val books: List<BookItemResponse>?
)