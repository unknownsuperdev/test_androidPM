package ru.tripster.data.repository.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.calendar.BusyIntervalsApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.сalendar.BusyIntervalRepository

class BusyIntervalRepositoryImpl(private val busyIntervalApiService: BusyIntervalsApiService) :
    BusyIntervalRepository {
    override suspend fun deleteBusyInterval(id: Int): ActionResult<Unit> =
        makeApiCall {
            analyzeResponse(busyIntervalApiService.deleteBusyInterval(id))
        }
}