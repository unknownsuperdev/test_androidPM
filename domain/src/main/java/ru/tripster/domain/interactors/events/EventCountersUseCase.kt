package ru.tripster.domain.interactors.events

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.events.EventCounters

interface EventCountersUseCase {
    suspend operator fun invoke() : ActionResult<EventCounters>
}