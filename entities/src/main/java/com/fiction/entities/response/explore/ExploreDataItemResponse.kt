package com.fiction.entities.response.explore

import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class ExploreDataItemResponse(
    @SerializedName("image_view")
    val imageView: String?,
    @SerializedName("items")
    val items: List<JsonElement>?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("url_collections")
    val urlCollections: String?
)
