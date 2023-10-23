package ru.tripster.domain.interactors.confirmOrEdit

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.confirmOrEdit.OrderConfirmOrEditData
import ru.tripster.domain.model.order.OrderDetails

interface OrderConfirmOrEditUseCase {
    suspend operator fun invoke(
        id: Int,
        confirmOrder: OrderConfirmOrEditData,
        isConfirm: Boolean
    ): ActionResult<OrderDetails>
}