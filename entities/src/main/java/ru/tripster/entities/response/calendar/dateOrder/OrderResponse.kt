package ru.tripster.entities.response.calendar.dateOrder

import com.google.gson.annotations.SerializedName
import ru.tripster.entities.response.events.LastMessageModel
import ru.tripster.entities.response.events.TravelerModel
import ru.tripster.entities.response.orders.RejectResponse

data class OrderResponse(
    @SerializedName("full_price")
    val full_price: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("last_message")
    val last_message: LastMessageModel?,
    @SerializedName("new_messages_count")
    val new_messages_count: Int?,
    @SerializedName("offsite_payment")
    val offsite_payment: Int?,
    @SerializedName("persons_count")
    val persons_count: Int?,
    @SerializedName("status")
    val status: String?,
    @SerializedName("traveler")
    val traveler: TravelerModel?
)