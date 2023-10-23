package ru.tripster.domain.repository.chat

import kotlinx.coroutines.flow.Flow
import ru.tripster.core.ActionResult
import ru.tripster.entities.request.chat.OrderCommentRequest
import ru.tripster.entities.request.chat.OrderCommentViewedRequest
import ru.tripster.entities.response.chat.OrderCommentsResponse
import ru.tripster.entities.response.chat.ResultResponse
import ru.tripster.entities.room.chat.OrderCommentsData

interface OrderCommentsRepository {
    suspend fun getOrderComments(orderId: Int, page: Int): ActionResult<OrderCommentsResponse>
    suspend fun postOrderComments(comment: OrderCommentRequest): ActionResult<ResultResponse>
    suspend fun insertCommentDB(comment: OrderCommentsData) : Long
    suspend fun getItemByKey(uuid: String): OrderCommentsData
    suspend fun getItemByMessageId(messageId: Int): OrderCommentsData
    suspend fun getItemId(id: Int): Boolean
    suspend fun getCommentsFromDb(orderId: Int): Flow<List<OrderCommentsData>>
    suspend fun chatIsOpened(
        commentId: Int,
        comment: OrderCommentViewedRequest
    ): ActionResult<ResultResponse>
}