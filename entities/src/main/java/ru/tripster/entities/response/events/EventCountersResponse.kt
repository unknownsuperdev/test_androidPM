package ru.tripster.entities.response.events

import com.google.gson.annotations.SerializedName

data class EventCountersResponse(
    @SerializedName("booked")
    val booked: Int?,
    @SerializedName("canceled")
    val canceled: Int?,
    @SerializedName("confirmation")
    val confirmation: Int?,
    @SerializedName("finished")
    val finished: Int?,
    @SerializedName("in_work")
    val in_work: Int?,
    @SerializedName("pending_payment")
    val pending_payment: Int?,
    @SerializedName("unread")
    val unread: Int?,
    @SerializedName("need_attention")
    val need_attention: Int?
)