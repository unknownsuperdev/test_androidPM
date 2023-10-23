package ru.tripster.domain.interactors.events

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.events.EventResults

interface GetCurrentGteEventUseCase {
    suspend operator fun invoke(
        groupOrderId: Int,
        guideLastVisitDate: String
    ): ActionResult<EventResults>
}