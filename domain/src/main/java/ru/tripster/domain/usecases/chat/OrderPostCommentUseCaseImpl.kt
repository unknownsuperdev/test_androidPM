package ru.tripster.domain.usecases.chat

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.chat.OrderPostCommentUseCase
import ru.tripster.domain.model.chat.OrderPostComment
import ru.tripster.domain.model.chat.OrderPostComment.Companion.from
import ru.tripster.domain.model.chat.UserRoleChat
import ru.tripster.domain.repository.chat.OrderCommentsRepository
import ru.tripster.entities.response.chat.SystemEventDataResponse
import ru.tripster.entities.response.chat.UserResponse
import ru.tripster.entities.room.chat.OrderCommentsData
import java.text.SimpleDateFormat
import java.util.*

class OrderPostCommentUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val orderCommentsRepository: OrderCommentsRepository,
) : OrderPostCommentUseCase {
    override suspend fun invoke(comment: OrderPostComment, key: String?): ActionResult<Unit> =
        withContext(dispatcher.io) {
            val uuid = if (key.isNullOrEmpty()) UUID.randomUUID().toString() else key
            val date =
                SimpleDateFormat(
                    "yyyy-MM-dd'T'HH:mm:ss",
                    Locale("ru")
                ).format(Calendar.getInstance().time)

            val insertComment = OrderCommentsData(
                comment = comment.comment,
                orderId = comment.order,
                userRole = UserRoleChat.GUIDE.value,
                submitDate = date,
                includeContacts = false,
                systemEventType = "",
                systemEventData = SystemEventDataResponse.empty(),
                user = UserResponse.empty(),
                viewedByGuide = true,
                viewedByTraveler = false,
                messageId = 0,
                key = uuid,
                error = "",
                nextPage = ""
            )

            orderCommentsRepository.insertCommentDB(
                if (uuid == key) orderCommentsRepository.getItemByKey(uuid).apply {
                    this.error = ""
                } else insertComment
            )

            when (val apiData = orderCommentsRepository.postOrderComments(comment.from())) {
                is ActionResult.Success -> {
                    apiData.result.let {

                        val updateItem = orderCommentsRepository.getItemByKey(uuid)
                        val updateItemNew = OrderCommentsData(
                            comment = it.comment ?: "",
                            orderId = comment.order,
                            userRole = UserRoleChat.GUIDE.value,
                            submitDate = it.submit_date ?: "",
                            includeContacts = it.include_contacts ?: false,
                            systemEventType = it.system_event_type ?: "",
                            systemEventData = it.system_event_data
                                ?: SystemEventDataResponse.empty(),
                            user = it.user ?: UserResponse.empty(),
                            viewedByGuide = it.viewed_by_guide ?: false,
                            viewedByTraveler = it.viewed_by_traveler ?: false,
                            messageId = it.id ?: 0,
                            key = uuid,
                            autogenId = updateItem.autogenId,
                            error = "",
                            nextPage = ""
                        )

                        orderCommentsRepository.insertCommentDB(updateItemNew)

                        ActionResult.Success(Unit)
                    }
                }

                is ActionResult.Error -> {
                    val updateItem = orderCommentsRepository.getItemByKey(uuid)
                    val errorItem = OrderCommentsData(
                        comment = comment.comment,
                        orderId = comment.order,
                        userRole = UserRoleChat.GUIDE.value,
                        submitDate = date,
                        includeContacts = false,
                        systemEventType = "",
                        systemEventData = SystemEventDataResponse.empty(),
                        user = UserResponse.empty(),
                        viewedByGuide = true,
                        viewedByTraveler = false,
                        messageId = 0,
                        key = uuid,
                        error = apiData.errors.message ?: "",
                        autogenId = updateItem.autogenId,
                        nextPage = ""
                    )

                    orderCommentsRepository.insertCommentDB(errorItem)
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}