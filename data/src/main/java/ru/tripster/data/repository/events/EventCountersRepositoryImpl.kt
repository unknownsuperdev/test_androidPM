package ru.tripster.data.repository.events

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.events.EventCountersApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.events.EventCountersRepository
import ru.tripster.entities.response.events.EventCountersResponse

class EventCountersRepositoryImpl(private val eventCountersApiService: EventCountersApiService) :
    EventCountersRepository {
    override suspend fun getEventCounters(): ActionResult<EventCountersResponse> = makeApiCall {
        analyzeResponse(eventCountersApiService.getEventCounters())
    }
}