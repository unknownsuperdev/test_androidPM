package ru.tripster.domain.interactors.chat

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.chat.OrderCommentViewed
import ru.tripster.domain.model.chat.OrderPostComment
import ru.tripster.entities.response.chat.ResultResponse

interface OrderCommentsChatOpenedUseCase {

    suspend operator fun invoke(commentId: Int, orderCommentViewed: OrderCommentViewed): ActionResult<Unit>
}
