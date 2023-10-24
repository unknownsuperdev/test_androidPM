package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class BookTemp(
    @SerializedName("book_id")
    val bookId: Long
)
