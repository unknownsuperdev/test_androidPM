package ru.tripster.domain.interactors.chat

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.chat.OrderCommentsResult
import ru.tripster.domain.model.chat.OrderPostComment

interface OrderPostCommentUseCase {
    suspend operator fun invoke(comment: OrderPostComment, key: String? = null): ActionResult<Unit>
}