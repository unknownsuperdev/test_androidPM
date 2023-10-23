package ru.tripster.domain.interactors.order

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.order.OrderCounterModel

interface OrderCounterUseCase {
    suspend operator fun invoke(): ActionResult<OrderCounterModel>
}
