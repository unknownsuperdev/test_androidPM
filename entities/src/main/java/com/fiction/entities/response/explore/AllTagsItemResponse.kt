package com.fiction.entities.response.explore


import com.google.gson.annotations.SerializedName

data class AllTagsItemResponse(
    @SerializedName("name")
    val name: String?,
    @SerializedName("tags")
    val tags: List<TagResponse>?
)