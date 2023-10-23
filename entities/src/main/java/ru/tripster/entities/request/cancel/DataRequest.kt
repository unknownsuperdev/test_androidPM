package ru.tripster.entities.request.cancel

import com.google.gson.annotations.SerializedName

data class DataRequest(
    @SerializedName("duplicate_date")
    val duplicate_date: String?,
    @SerializedName("duplicate_order")
    val duplicate_order: Int?
)