package ru.tripster.data.repository.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.calendar.CalendarApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.сalendar.CalendarRepository
import ru.tripster.entities.response.calendar.GuidesScheduleResponse
import ru.tripster.entities.response.calendar.dateOrder.GuideDateScheduleResponse

class CalendarRepositoryImpl(
    private val calendarApiService: CalendarApiService
) : CalendarRepository {
    override suspend fun getGuidesSchedule(
        begin: String,
        end: String,
        experienceId: Int?,
        showEventData: Boolean?
    ): ActionResult<List<GuidesScheduleResponse>> =
        makeApiCall {
            analyzeResponse(calendarApiService.getGuidesSchedule(begin, end, experienceId, showEventData))
        }

    override suspend fun getGuideDateSchedule(
        date: String,
        experienceId: Int?
    ): ActionResult<GuideDateScheduleResponse> =
        makeApiCall {
            analyzeResponse(
                calendarApiService.getGuidesDateSchedule(
                    date,
                    experienceId
                )
            )
        }
}