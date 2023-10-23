package ru.tripster.domain.usecases.chat

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.Constants.Companion.CHAT_CANCEL_COMMENT_ADD_TO_ID
import ru.tripster.domain.interactors.chat.OrderCommentsInsertUseCase
import ru.tripster.domain.model.chat.OrderComments
import ru.tripster.domain.model.chat.OrderCommentsResult
import ru.tripster.domain.model.chat.OrderCommentsResult.Companion.fromResponse
import ru.tripster.domain.model.chat.UserRoleChat
import ru.tripster.domain.model.chat.UserStatusChat
import ru.tripster.domain.repository.chat.OrderCommentsRepository
import ru.tripster.entities.response.chat.SystemEventDataResponse
import ru.tripster.entities.response.chat.UserResponse
import ru.tripster.entities.room.chat.OrderCommentsData
import java.util.*

class OrderCommentsInsertUseCaseImpl(
    private val orderCommentsRepository: OrderCommentsRepository,
    private val dispatcher: CoroutineDispatcherProvider,
) : OrderCommentsInsertUseCase {

    private val unreadMessageList = mutableListOf<OrderCommentsData>()
    private val uuid = UUID.randomUUID().toString()
    override suspend fun invoke(
        orderId: Int,
        page: Int,
        currency: String
    ): ActionResult<OrderComments> =
        withContext(dispatcher.io) {

            when (val apiData = orderCommentsRepository.getOrderComments(orderId, page)) {
                is ActionResult.Success -> {
                    apiData.result.results?.forEach {
                        it.id?.let { id ->
                            if (!orderCommentsRepository.getItemId(id)) {
                                orderCommentsRepository.insertCommentDB(
                                    OrderCommentsResult.fromResultResponse(it)
                                        .fromResponse(it, orderId, currency, apiData.result.next ?: "" )
                                )

                                /* Rejected order comment entrance */

                                if (it.system_event_type == UserStatusChat.REJECT.value && !it.comment.isNullOrEmpty() && it.user_role == UserRoleChat.GUIDE.value) {
                                    orderCommentsRepository.insertCommentDB(
                                        OrderCommentsData(
                                            comment = it.comment ?: "",
                                            orderId = orderId,
                                            userRole = UserRoleChat.GUIDE.value,
                                            submitDate = it.submit_date ?: "",
                                            includeContacts = it.include_contacts ?: false,
                                            systemEventType = "",
                                            systemEventData = SystemEventDataResponse.empty(),
                                            user = it.user ?: UserResponse.empty(),
                                            viewedByGuide = it.viewed_by_guide ?: false,
                                            viewedByTraveler = it.viewed_by_traveler ?: false,
                                            messageId = id + CHAT_CANCEL_COMMENT_ADD_TO_ID,
                                            key = uuid,
                                            nextPage = apiData.result.next ?: ""
                                        )
                                    )
                                } else if (it.system_event_type == UserStatusChat.REJECT.value && !it.comment.isNullOrEmpty() && it.user_role == UserRoleChat.TRAVELER.value) {
                                    orderCommentsRepository.insertCommentDB(
                                        OrderCommentsData(
                                            comment = it.comment ?: "",
                                            orderId = orderId,
                                            userRole = UserRoleChat.TRAVELER.value,
                                            submitDate = it.submit_date ?: "",
                                            includeContacts = it.include_contacts ?: false,
                                            systemEventType = "",
                                            systemEventData = SystemEventDataResponse.empty(),
                                            user = it.user ?: UserResponse.empty(),
                                            viewedByGuide = it.viewed_by_guide ?: false,
                                            viewedByTraveler = it.viewed_by_traveler ?: false,
                                            messageId = id + CHAT_CANCEL_COMMENT_ADD_TO_ID,
                                            key = uuid,
                                            nextPage = apiData.result.next ?: ""
                                        )
                                    )
                                }
                                unreadMessageList.add(
                                    OrderCommentsResult.fromResultResponse(it)
                                        .fromResponse(it, orderId, currency, apiData.result.next ?: "")
                                )
                            }

                            if (orderCommentsRepository.getItemByMessageId(id).viewedByTraveler != it.viewed_by_traveler) {
                                orderCommentsRepository.insertCommentDB(
                                    orderCommentsRepository.getItemByMessageId(id)
                                        .apply {
                                            this.viewedByTraveler = it.viewed_by_traveler ?: false
                                        }
                                )

                                if (orderCommentsRepository.getItemId(id + CHAT_CANCEL_COMMENT_ADD_TO_ID)) {
                                    orderCommentsRepository.insertCommentDB(
                                        orderCommentsRepository.getItemByMessageId(id + CHAT_CANCEL_COMMENT_ADD_TO_ID)
                                            .apply {
                                                this.viewedByTraveler =
                                                    it.viewed_by_traveler ?: false
                                            }
                                    )
                                }
                            }

                            if (orderCommentsRepository.getItemByMessageId(id).viewedByGuide != it.viewed_by_guide
                                && unreadMessageList.none { i -> i.messageId == it.id }
                            ) {
                                orderCommentsRepository.insertCommentDB(
                                    orderCommentsRepository.getItemByMessageId(id)
                                        .apply {
                                            this.viewedByGuide = it.viewed_by_guide ?: false
                                        }
                                )
                            }

                            if (orderCommentsRepository.getItemByMessageId(id).comment != it.comment) {
                                orderCommentsRepository.insertCommentDB(
                                    orderCommentsRepository.getItemByMessageId(id).apply {
                                        this.comment = it.comment.toString()
                                    }
                                )
                            }

                        }
                    }
                    ActionResult.Success(OrderComments.from(apiData.result.apply { this.results }))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}




