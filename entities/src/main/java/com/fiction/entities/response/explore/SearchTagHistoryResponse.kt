package com.fiction.entities.response.explore

import com.google.gson.annotations.SerializedName

class SearchTagHistoryResponse(
    @SerializedName("name")
    val tagName: String?
)