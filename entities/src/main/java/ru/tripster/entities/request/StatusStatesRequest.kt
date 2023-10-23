package ru.tripster.entities.request

import com.google.gson.annotations.SerializedName

enum class StatusStatesRequest(val number: Int) {
    @SerializedName("1")
    CONFIRM(1),
    @SerializedName("2")
    PENDING_PAYMENT(2),
    @SerializedName("3")
    BOOKED(3),
    @SerializedName("4")
    CANCELLED(4)
}