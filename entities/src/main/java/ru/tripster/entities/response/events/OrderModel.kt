package ru.tripster.entities.response.events

import com.google.gson.annotations.SerializedName
import ru.tripster.entities.response.orders.RejectResponse

data class OrderModel(
    @SerializedName("full_price")
    val full_price: Double?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("offsite_payment")
    val offsite_payment: Int?,
    @SerializedName("persons_count")
    val persons_count: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("traveler")
    val traveler: TravelerModel?,
    @SerializedName("last_message")
    val last_message: LastMessageModel?,
    @SerializedName("new_messages_count")
    val new_messages_count: Int?,
    @SerializedName("reject")
    val reject: RejectResponse? = null
)