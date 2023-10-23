package ru.tripster.data.repository.chat

import kotlinx.coroutines.flow.Flow
import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.chat.ChatApiService
import ru.tripster.data.dataservice.sqlservice.chat.OrderCommentsDao
import ru.tripster.entities.room.chat.OrderCommentsData
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.chat.OrderCommentsRepository
import ru.tripster.entities.request.chat.OrderCommentRequest
import ru.tripster.entities.request.chat.OrderCommentViewedRequest
import ru.tripster.entities.response.chat.OrderCommentsResponse
import ru.tripster.entities.response.chat.ResultResponse

class OrderCommentsRepositoryImpl(
    private val chatApiService: ChatApiService,
    private val orderCommentsDao: OrderCommentsDao
) : OrderCommentsRepository {
    override suspend fun getOrderComments(
        orderId: Int,
        page: Int
    ): ActionResult<OrderCommentsResponse> = makeApiCall {
        analyzeResponse(chatApiService.getOrderComments(orderId, page, 30))
    }

    override suspend fun postOrderComments(comment: OrderCommentRequest): ActionResult<ResultResponse> =
        makeApiCall {
            analyzeResponse(chatApiService.postOrderComment(comment))
        }

    override suspend fun insertCommentDB(comment: OrderCommentsData):Long =
        orderCommentsDao.insertData(comment)

    override suspend fun getItemByKey(uuid: String): OrderCommentsData =
        orderCommentsDao.getItemByKey(uuid)

    override suspend fun getItemByMessageId(messageId: Int): OrderCommentsData =
        orderCommentsDao.getItemByMessageId(messageId)


    override suspend fun getItemId(id: Int): Boolean = orderCommentsDao.getItemById(id)

    override suspend fun getCommentsFromDb(orderId: Int): Flow<List<OrderCommentsData>> =
        orderCommentsDao.getCommentsFromDb(orderId)

    override suspend fun chatIsOpened(
        commentId: Int,
        comment: OrderCommentViewedRequest
    ): ActionResult<ResultResponse> = makeApiCall {
        analyzeResponse(chatApiService.chatIsOpened(commentId, comment))
    }
}