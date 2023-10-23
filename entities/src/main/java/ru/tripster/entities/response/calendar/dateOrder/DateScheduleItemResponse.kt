package ru.tripster.entities.response.calendar.dateOrder

import com.google.gson.annotations.SerializedName

data class DateScheduleItemResponse(
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("event_data")
    val event_data: EventDataResponse?,
    @SerializedName("experience_name")
    val experience_name: String?,
    @SerializedName("experience_id")
    val experience_id: Int?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("comment")
    val comment: String?
)