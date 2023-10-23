package ru.tripster.domain.model.chat

import ru.tripster.core.DiffUtilModel
import ru.tripster.entities.request.chat.OrderCommentRequest
import ru.tripster.entities.request.chat.OrderCommentViewedRequest
import ru.tripster.entities.response.chat.*
import ru.tripster.entities.room.chat.OrderCommentsData

data class OrderComments(
    val count: Int,
    val next: String,
    val previous: String,
    val results: List<OrderCommentsModel>
) {
    companion object {
        fun from(result: OrderCommentsResponse): OrderComments = with(result) {
            OrderComments(
                count = count ?: 0,
                next = next ?: "",
                previous = previous ?: "",
                results = results?.let { list ->
                    list.map {
                        OrderCommentsResult.fromResultResponse(
                            it
                        )
                    }
                }
                    ?: emptyList()
            )
        }
    }
}

data class OrderCommentsResult(
    val comment: String,
    override val id: Int,
    val includeContacts: Boolean,
    val submitDate: String,
    val systemEventData: SystemEventData,
    val systemEventType: String,
    val user: User,
    val viewedByGuide: Boolean,
    val viewedByTraveler: Boolean,
    val userRole: String,
    val key: String? = null,
    val error: String? = null,
    val currency: String? = null,
    val addHeaderToMessage: Boolean? = null,
    val ordersRate: Boolean = false,
    val travelerContactsOpen: Boolean = false,
    val isLastMessage: Boolean = false,
    val prevMsgIsFromOtherUser: Boolean = false,
    val prevMsgIsFromGuide: Boolean = false,
    val prevMsgHaveHeader: Boolean = false,
    val nextPage: String = ""
) : OrderCommentsModel(id) {
    companion object {
        fun fromResultResponse(result: ResultResponse): OrderCommentsResult = with(result) {
            OrderCommentsResult(
                comment = comment ?: "",
                id = id ?: 0,
                includeContacts = include_contacts ?: false,
                submitDate = submit_date ?: "",
                systemEventData = system_event_data?.let { SystemEventData.from(it) }
                    ?: SystemEventData.empty(),
                systemEventType = system_event_type ?: "",
                user = user?.let { User.from(it) } ?: User.empty(),
                viewedByGuide = viewed_by_guide ?: false,
                viewedByTraveler = viewed_by_traveler ?: false,
                userRole = user_role ?: ""
            )
        }

        fun fromOrderCommentsData(result: OrderCommentsData): OrderCommentsModel = with(result) {
            OrderCommentsResult(
                comment = comment,
                id = messageId,
                includeContacts = includeContacts,
                submitDate = submitDate,
                systemEventData = systemEventData.let { SystemEventData.from(it) },
                systemEventType = systemEventType,
                user = user.let { User.from(it) },
                viewedByGuide = viewedByGuide,
                viewedByTraveler = viewedByTraveler,
                userRole = userRole,
                error = error,
                key = key,
                currency = currency,
                nextPage = nextPage
            )
        }

        fun OrderCommentsResult.fromResponse(
            result: ResultResponse,
            orderId: Int,
            currency: String,
            nextPage: String
        ): OrderCommentsData =
            with(result) {
                OrderCommentsData(
                    comment = comment ?: "",
                    messageId = id ?: 0,
                    includeContacts = include_contacts ?: false,
                    submitDate = submit_date ?: "",
                    systemEventData = system_event_data ?: SystemEventDataResponse.empty(),
                    systemEventType = system_event_type ?: "",
                    user = user ?: UserResponse.empty(),
                    orderId = orderId,
                    viewedByGuide = viewed_by_guide ?: false,
                    viewedByTraveler = viewedByTraveler,
                    userRole = userRole,
                    key = key ?: "",
                    error = error ?: "",
                    currency = currency,
                    nextPage = nextPage
                )
            }
    }
}

data class OrderCommentsHeader(
    override val id: Int,
    val memberCountAndPrice: String?,
    val avatar: String,
    val name: String
) : OrderCommentsModel(id)

open class OrderCommentsModel(override val id: Int) :
    DiffUtilModel<Int>()

data class OrderPostComment(
    val comment: String,
    val order: Int,
    val postContacts: Boolean,
    val userRole: String,
    val viewedByGuide: Boolean
) {
    companion object {
        fun OrderPostComment.from(): OrderCommentRequest =
            OrderCommentRequest(
                comment = comment,
                order = order,
                post_contacts = postContacts,
                user_role = userRole,
                viewed_by_guide = viewedByGuide
            )
    }
}

data class OrderCommentViewed(
    val viewedByGuide: Boolean
) {
    companion object {
        fun OrderCommentViewed.from(): OrderCommentViewedRequest =
            OrderCommentViewedRequest(
                viewed_by_guide = viewedByGuide
            )
    }
}

data class SystemEventData(
    val changes: Changes,
    val numberOfPersons: Int,
    val reason: String,
    val tickets: List<Ticket>
) {
    companion object {
        fun from(result: SystemEventDataResponse): SystemEventData = with(result) {
            SystemEventData(
                changes = changes?.let { Changes.from(it) } ?: Changes.empty(),
                numberOfPersons = number_of_persons ?: 0,
                reason = reason ?: "",
                tickets = tickets?.let { list -> list.map { Ticket.from(it) } } ?: emptyList()
            )
        }

        fun empty(): SystemEventData =
            SystemEventData(
                changes = Changes.empty(),
                numberOfPersons = 0,
                reason = "",
                tickets = emptyList()
            )
    }
}

data class Changes(
    val confirmed: Confirmed,
    val dateTime: DateTime,
    val fullPrice: FullPrice,
    val personsCount: PersonsCount,
    val status: Status,
    val tickets: Tickets
) {
    companion object {
        fun from(result: ChangesResponse): Changes = with(result) {
            Changes(
                confirmed = confirmed?.let { Confirmed.from(it) } ?: Confirmed.empty(),
                dateTime = date_time?.let { DateTime.from(it) } ?: DateTime.empty(),
                fullPrice = full_price?.let { FullPrice.from(it) } ?: FullPrice.empty(),
                personsCount = persons_count?.let { PersonsCount.from(it) } ?: PersonsCount.empty(),
                status = status?.let { Status.from(it) } ?: Status.empty(),
                tickets = tickets?.let { Tickets.from(it) } ?: Tickets.empty()
            )
        }

        fun empty(): Changes =
            Changes(
                confirmed = Confirmed.empty(),
                dateTime = DateTime.empty(),
                fullPrice = FullPrice.empty(),
                personsCount = PersonsCount.empty(),
                status = Status.empty(),
                tickets = Tickets.empty()
            )
    }
}

data class Confirmed(
    val newValue: Boolean,
    val oldValue: Boolean
) {
    companion object {
        fun from(result: ConfirmedResponse): Confirmed = with(result) {
            Confirmed(
                newValue = new_value ?: false,
                oldValue = old_value ?: false
            )
        }

        fun empty(): Confirmed =
            Confirmed(
                newValue = false,
                oldValue = false
            )
    }
}

data class DateTime(
    val newValue: String,
    val oldValue: String
) {
    companion object {
        fun from(result: DateTimeResponse): DateTime = with(result) {
            DateTime(
                newValue = new_value ?: "",
                oldValue = old_value ?: ""
            )
        }

        fun empty(): DateTime =
            DateTime(
                newValue = "",
                oldValue = ""
            )
    }
}

data class FullPrice(
    val newValue: Double,
    val oldValue: Double
) {
    companion object {
        fun from(result: FullPriceResponse): FullPrice = with(result) {
            FullPrice(
                newValue = new_value ?: 0.0,
                oldValue = old_value ?: 0.0
            )
        }

        fun empty(): FullPrice =
            FullPrice(
                newValue = 0.0,
                oldValue = 0.0
            )
    }
}

data class PersonsCount(
    val newValue: Int,
    val oldValue: Int
) {
    companion object {
        fun from(result: PersonsCountResponse): PersonsCount = with(result) {
            PersonsCount(
                newValue = new_value ?: 0,
                oldValue = old_value ?: 0
            )
        }

        fun empty(): PersonsCount =
            PersonsCount(
                newValue = 0,
                oldValue = 0
            )
    }
}

data class AdditionalProp(
    val additionalProp1: String,
    val additionalProp2: String,
    val additionalProp3: String
) {
    companion object {
        fun from(result: AdditionalPropResponse): AdditionalProp = with(result) {
            AdditionalProp(
                additionalProp1 = additionalProp1 ?: "",
                additionalProp2 = additionalProp2 ?: "",
                additionalProp3 = additionalProp3 ?: "",
            )
        }

        fun empty(): AdditionalProp =
            AdditionalProp(
                additionalProp1 = "",
                additionalProp2 = "",
                additionalProp3 = "",
            )
    }
}

data class Status(
    val newValue: String,
    val oldValue: String
) {
    companion object {
        fun from(result: StatusResponse): Status = with(result) {
            Status(
                newValue = new_value ?: "",
                oldValue = old_value ?: ""
            )
        }

        fun empty(): Status =
            Status(
                newValue = "",
                oldValue = ""
            )
    }
}

data class Tickets(
    val newValue: List<AdditionalProp>,
    val oldValue: List<AdditionalProp>
) {
    companion object {
        fun from(result: TicketsResponse): Tickets = with(result) {
            Tickets(
                newValue = new_value?.let { list -> list.map { AdditionalProp.from(it) } }
                    ?: emptyList(),
                oldValue = old_value?.let { list -> list.map { AdditionalProp.from(it) } }
                    ?: emptyList()
            )
        }

        fun empty(): Tickets =
            Tickets(
                newValue = emptyList(),
                oldValue = emptyList()
            )
    }
}

data class Ticket(
    val count: Int,
    val name: String,
    val price: Int
) {
    companion object {
        fun from(result: TicketResponse): Ticket = with(result) {
            Ticket(
                count = count ?: 0,
                name = name ?: "",
                price = price ?: 0
            )
        }
    }
}

data class User(
    val avatar150Url: String?,
    val avatar30Url: String?,
    val firstName: String?,
    val id: Int?
) {
    companion object {
        fun from(result: UserResponse): User = with(result) {
            User(
                avatar150Url = avatar150_url ?: "",
                avatar30Url = avatar30_url ?: "",
                firstName = first_name ?: "",
                id = id ?: 0
            )
        }

        fun empty(): User =
            User(
                avatar150Url = "",
                avatar30Url = "",
                firstName = "",
                id = 0
            )
    }
}

data class OrderCommentGetModel(
    val id: Int,
    val header: OrderCommentsHeader? = null,
    val currency: String,
    val canOpenContacts: Boolean,
    val ordersRateValue: Int,
) {
    companion object {
        fun empty(): OrderCommentGetModel =
            OrderCommentGetModel(
                0,
                null,
                "",
                false,
                0
            )
    }
}
