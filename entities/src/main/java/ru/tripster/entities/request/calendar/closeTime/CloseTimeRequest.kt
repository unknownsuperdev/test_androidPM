package ru.tripster.entities.request.calendar.closeTime

import com.google.gson.annotations.SerializedName

data class CloseTimeRequest(
    @SerializedName("comment")
    val comment: String?,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("end_date")
    val end_date: String,
    @SerializedName("experience")
    val experience: Int?,
    @SerializedName("start_date")
    val start_date: String,
    @SerializedName("start_time")
    val start_time: String
)