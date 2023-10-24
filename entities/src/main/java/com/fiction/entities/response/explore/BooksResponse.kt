package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class BooksResponse(
    @SerializedName("items")
    val items: List<BookItemResponse>
)