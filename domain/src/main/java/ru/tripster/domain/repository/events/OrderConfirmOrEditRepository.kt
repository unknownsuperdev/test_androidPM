package ru.tripster.domain.repository.events

import ru.tripster.core.ActionResult
import ru.tripster.entities.request.confirm.OrderConfirmRequest
import ru.tripster.entities.response.experience.ChangeExperienceResponse
import ru.tripster.entities.response.orders.OrderDetailsResponse

interface OrderConfirmOrEditRepository {
    suspend fun confirmOrder(
        id: Int,
        confirmOrder: OrderConfirmRequest
    ): ActionResult<OrderDetailsResponse>

    suspend fun editOrder(
        id: Int,
        confirmOrder: OrderConfirmRequest
    ): ActionResult<OrderDetailsResponse>

    suspend fun changeExperience(
        experienceId: Int,
        personsCount: Int,
        date: String,
        tickets: String?,
        customPrice: Double?,
        time: String?
    ): ActionResult<ChangeExperienceResponse>
}