package ru.tripster.entities.request.confirm

import com.google.gson.annotations.SerializedName

data class OrderConfirmRequest(
    @SerializedName("auto_set_day_busy")
    val auto_set_day_busy: Boolean?,
    @SerializedName("custom_price")
    val custom_price: Int?,
    @SerializedName("date_exact")
    val date_exact: String?,
    @SerializedName("email")
    val email: String?,
    @SerializedName("meeting_point")
    val meeting_point: String?,
    @SerializedName("persons_count")
    val persons_count: Int?,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("tickets")
    val tickets: List<TicketRequest>?,
    @SerializedName("time_start")
    val time_start: String?,
    @SerializedName("traveler_contacts_open")
    val traveler_contacts_open: Boolean?
)