package ru.tripster.entities.responce.response.statistics

import com.google.gson.annotations.SerializedName

data class OrderStatisticsResponse(
    @SerializedName("booking_rate")
    val booking_rate: BookingRateResponse?,
    @SerializedName("can_open_traveler_contacts")
    val can_open_traveler_contacts: Boolean?,
    @SerializedName("confirmation_rate")
    val confirmation_rate: ConfirmationRateResponse?,
    @SerializedName("not_enough_orders")
    val not_enough_orders: Boolean?,
    @SerializedName("orders_rate")
    val orders_rate: OrdersRateResponse?,
    @SerializedName("reaction_delay")
    val reaction_delay: ReactionDelayResponse?
)