package ru.tripster.data.repository.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.calendar.CalendarApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.сalendar.CloseTimeRepository
import ru.tripster.entities.request.calendar.closeTime.CloseTimeRequest

class CloseTimeRepositoryImpl(private val calendarApiService: CalendarApiService) :
    CloseTimeRepository {
    override suspend fun addCloseTime(closeTime: CloseTimeRequest): ActionResult<Unit> =
        makeApiCall {
            analyzeResponse(calendarApiService.addBusySchedule(closeTime))
        }
}