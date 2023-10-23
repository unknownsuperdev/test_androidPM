package ru.tripster.entities.request.cancel

import com.google.gson.annotations.SerializedName

data class OrderCancelRequest(
    @SerializedName("key")
    val key: Int,
    @SerializedName("message")
    val message: String?,
    @SerializedName("data")
    val data: DataRequest?
)