package ru.tripster.domain.repository.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.entities.response.calendar.GuidesScheduleResponse
import ru.tripster.entities.response.calendar.dateOrder.GuideDateScheduleResponse

interface CalendarRepository {
    suspend fun getGuidesSchedule(
        begin: String,
        end: String,
        experienceId: Int?,
        showEventData: Boolean?
    ): ActionResult<List<GuidesScheduleResponse>>

    suspend fun getGuideDateSchedule(
        date: String,
        experienceId: Int?
    ): ActionResult<GuideDateScheduleResponse>
}