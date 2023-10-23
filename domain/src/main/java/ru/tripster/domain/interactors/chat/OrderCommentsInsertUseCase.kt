package ru.tripster.domain.interactors.chat

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.chat.OrderComments

interface OrderCommentsInsertUseCase {
    suspend operator fun invoke(orderId: Int, page: Int,currency: String) : ActionResult<OrderComments>
}