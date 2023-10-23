package ru.tripster.domain.interactors.confirmOrEdit

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.order.ChangeExperience
import ru.tripster.domain.model.order.PerTicket
import ru.tripster.domain.model.order.TicketType
import ru.tripster.entities.request.confirm.TicketRequest

interface ChangeExperienceUseCase {
    suspend fun invoke(
        experienceId: Int,
        personsCount: Int,
        date: String,
        tickets: List<TicketType>?,
        customPrice: Double? = null,
        time:String? = null
    ): ActionResult<ChangeExperience>
}