package ru.tripster.domain.usecases.chat

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.chat.OrderCommentsChatOpenedUseCase
import ru.tripster.domain.model.chat.OrderCommentViewed
import ru.tripster.domain.model.chat.OrderCommentViewed.Companion.from
import ru.tripster.domain.repository.chat.OrderCommentsRepository

class OrderCommentsChatOpenedUseCaseImpl(
    private val orderCommentsRepository: OrderCommentsRepository,
    private val dispatcher: CoroutineDispatcherProvider,
) : OrderCommentsChatOpenedUseCase {

    override suspend fun invoke(
        commentId: Int,
        orderCommentViewed: OrderCommentViewed
    ): ActionResult<Unit> = withContext(dispatcher.io) {
        when (val apiData = orderCommentsRepository.chatIsOpened(commentId,orderCommentViewed.from())) {
            is ActionResult.Success -> {
                ActionResult.Success(Unit)
            }

            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}
