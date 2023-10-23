package ru.tripster.domain.interactors.order

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.order.OrderDetails

interface OrderDetailsUseCase {
    suspend operator fun invoke(id:Int) : ActionResult<OrderDetails>
}