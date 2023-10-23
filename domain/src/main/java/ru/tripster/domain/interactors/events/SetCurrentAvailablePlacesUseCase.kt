package ru.tripster.domain.interactors.events

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.events.EventResults

interface SetCurrentAvailablePlacesUseCase {
    suspend operator fun invoke(groupOrderId:Int,availablePlaces: Int,lastrVisitDate:String): ActionResult<EventResults>
}