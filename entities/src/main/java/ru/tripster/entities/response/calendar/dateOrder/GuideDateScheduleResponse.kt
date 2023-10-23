package ru.tripster.entities.response.calendar.dateOrder

import com.google.gson.annotations.SerializedName

data class GuideDateScheduleResponse(
    @SerializedName("date")
    val date: String?,
    @SerializedName("event_count")
    val event_count: Int?,
    @SerializedName("event_total_count")
    val event_total_count: Int?,
    @SerializedName("items")
    val guideScheduleItemResponses: List<DateScheduleItemResponse>?
)