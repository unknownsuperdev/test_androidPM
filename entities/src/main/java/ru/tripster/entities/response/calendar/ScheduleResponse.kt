package ru.tripster.entities.response.calendar

import com.google.gson.annotations.SerializedName
import ru.tripster.entities.response.calendar.dateOrder.EventDataResponse

data class ScheduleResponse(
    @SerializedName("type")
    val type: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("time")
    val time: String?,
    @SerializedName("experience_id")
    val experience_id: Int?,
    @SerializedName("experience_name")
    val experience_name: String?,
    @SerializedName("duration")
    val duration: Int?,
    @SerializedName("event_data")
    val event_data: EventDataResponse?
)