package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class BookInfoResponse(
    @SerializedName("book")
    val book: BookItemResponse?,
    @SerializedName("progress")
    val progress: ReadingProgressResponse?
)