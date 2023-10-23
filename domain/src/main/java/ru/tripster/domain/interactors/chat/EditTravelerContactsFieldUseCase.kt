package ru.tripster.domain.interactors.chat

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.confirmOrEdit.EditTravelerContactsOpen
import ru.tripster.domain.model.order.OrderDetails
import ru.tripster.entities.request.chat.TravelerContactsOpenRequest

interface EditTravelerContactsFieldUseCase {
    suspend operator fun invoke(
        id: Int,
        travelerContactsOpen: EditTravelerContactsOpen
    ): ActionResult<OrderDetails>
}