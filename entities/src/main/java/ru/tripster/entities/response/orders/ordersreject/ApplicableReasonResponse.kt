package ru.tripster.entities.response.orders.ordersreject

import com.google.gson.annotations.SerializedName

data class ApplicableReasonResponse(
    @SerializedName("key")
    val key: Int?,
    @SerializedName("latin_key")
    val latinKey: String?,
    @SerializedName("verbose_name")
    val verboseName: String?
)