package ru.tripster.entities.response

import com.google.gson.annotations.SerializedName

data class DataInfoResponse(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int,
    @SerializedName("height")
    val height: Int,
    @SerializedName("box_count")
    val boxCount: String,
)