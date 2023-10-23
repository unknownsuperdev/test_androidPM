package ru.tripster.entities.response.orders

import com.google.gson.annotations.SerializedName

data class OrderCounterResponse(
    @SerializedName("need_attention")
    val need_attention: Int?
)
