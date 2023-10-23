package ru.tripster.domain.interactors.events

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.events.EventResults

interface GetOrderEventUseCase {
    suspend operator fun invoke(orderId: Int): ActionResult<EventResults>
}
