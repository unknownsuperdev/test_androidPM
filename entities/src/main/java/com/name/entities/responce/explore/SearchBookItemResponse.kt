package com.name.entities.responce.explore

import com.google.gson.annotations.SerializedName

data class SearchBookItemResponse(
    @SerializedName("book")
    var searchBookWithName: List<SearchBookWithNameResponse>?,
    @SerializedName("tags")
    var searchBookWithTag: List<SearchBookWithTagResponse>?
)
