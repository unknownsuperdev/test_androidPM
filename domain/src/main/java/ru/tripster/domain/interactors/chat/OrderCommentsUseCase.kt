package ru.tripster.domain.interactors.chat

import kotlinx.coroutines.flow.Flow
import ru.tripster.domain.model.chat.OrderCommentGetModel
import ru.tripster.domain.model.chat.OrderComments
import ru.tripster.domain.model.chat.OrderCommentsHeader
import ru.tripster.domain.model.chat.OrderCommentsModel

interface OrderCommentsUseCase {
    suspend operator fun invoke(model:OrderCommentGetModel) : Flow<List<OrderCommentsModel?>>
}