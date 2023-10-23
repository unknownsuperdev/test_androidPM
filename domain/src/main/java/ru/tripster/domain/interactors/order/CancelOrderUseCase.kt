package ru.tripster.domain.interactors.order

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.cancel.OrderCancelData

interface CancelOrderUseCase {
    suspend operator fun invoke(id: Int, cancelOrder: OrderCancelData): ActionResult<Boolean>
}