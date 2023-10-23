package ru.tripster.entities.response.orders.ordersreject

import com.google.gson.annotations.SerializedName

data class DuplicateOrderResponse(
    @SerializedName("date")
    val date: String?,
    @SerializedName("experience_id")
    val experienceId: Int?,
    @SerializedName("exp_type")
    val expType: String?,
    @SerializedName("experience_title")
    val experienceTitle: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("traveler_name")
    val travelerName: String?
)