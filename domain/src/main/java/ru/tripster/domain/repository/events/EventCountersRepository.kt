package ru.tripster.domain.repository.events

import ru.tripster.core.ActionResult
import ru.tripster.entities.response.events.EventCountersResponse

interface EventCountersRepository {
    suspend fun getEventCounters(): ActionResult<EventCountersResponse>
}