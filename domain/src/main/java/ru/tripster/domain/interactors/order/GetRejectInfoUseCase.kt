package ru.tripster.domain.interactors.order

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.cancel.OrderCancelData
import ru.tripster.domain.model.confirmOrEdit.OrderConfirmOrEditData
import ru.tripster.domain.model.order.orderrejectinfo.RejectInfo


interface GetRejectInfoUseCase {
    suspend operator fun invoke(id:Int) :ActionResult<RejectInfo>
}