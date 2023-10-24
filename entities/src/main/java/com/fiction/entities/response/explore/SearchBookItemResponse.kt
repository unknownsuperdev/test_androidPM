package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

data class SearchBookItemResponse(
    @SerializedName("books")
    var searchBookWithName: List<BookItemResponse>?,
    @SerializedName("tags")
    var searchBookWithTag: List<SearchBookWithTagResponse>?
)
