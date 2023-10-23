package ru.tripster.entities.response.calendar

import com.google.gson.annotations.SerializedName

data class GuidesScheduleResponse(
    @SerializedName("date")
    val date: String?,
    @SerializedName("items")
    val guideScheduleResponses: List<ScheduleResponse>?,
)
