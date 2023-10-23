package ru.tripster.domain.model.events

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.tripster.core.DiffUtilModel
import ru.tripster.domain.model.order.Reject
import ru.tripster.domain.model.statistics.OrderStatisticsData
import ru.tripster.entities.response.events.*
import java.time.LocalDate

data class Events(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<EventResults>?
) {
    companion object {
        fun from(events: EventsResponse): Events =
            with(events) {
                Events(
                    count = count,
                    next = next,
                    previous = previous,
                    results = results?.map { EventResults.from(it) }
                )
            }
    }
}

data class EventResults(
    override val id: Int,
    val experience: Experience,
    val isForGroups: Boolean,
    val maxPersons: Int,
    val numberOfPersonsAvail: Int,
    val numberOfPersonsOverall: Int,
    val numberOfPersonsPaid: Int,
    val orders: SortedOrders,
    val time: String,
    val date: String,
    val status: String,
    val guideLastVisitDate: String,
    val lastModifiedDate: String,
    val awareStartDt: String,
    val orderRateValue: Int
) : DiffUtilModel<Int>() {

    companion object {
        fun from(result: ResultModel, statistics: Int = 0): EventResults =
            with(result) {
                EventResults(
                    experience = experience?.let { Experience.from(it) } ?: Experience.emptyItem(),
                    id = id ?: 0,
                    isForGroups = is_for_groups ?: false,
                    maxPersons = max_persons ?: 0,
                    numberOfPersonsAvail = number_of_persons_avail ?: 0,
                    numberOfPersonsOverall = number_of_persons_overall ?: 0,
                    numberOfPersonsPaid = number_of_persons_paid ?: 0,
                    orders = orders?.map { Order.from(it) }?.let { SortedOrders.from(it) }
                        ?: SortedOrders.from(emptyList()),
                    date = date ?: "",
                    time = time ?: "",
                    status = status ?: "",
                    guideLastVisitDate = guide_last_visit_date ?: "",
                    awareStartDt = aware_start_dt ?: "",
                    orderRateValue = statistics,
                    lastModifiedDate = last_modified_date ?: ""
                )
            }
    }
}

@Parcelize
data class SortedOrders(
    val paid: List<Order>,
    val pendingPayment: List<Order>,
    val confirmation: List<Order>,
    val cancelled: List<Order>,
    val messaging: List<Order>,
    val empty: List<Order>
) : Parcelable {
    companion object {
        fun from(order: List<Order>): SortedOrders =
            SortedOrders(
                paid = order.filter { it.status == Status.PAID.value },
                pendingPayment = order.filter { it.status == Status.PENDING_PAYMENT.value },
                confirmation = order.filter { it.status == Status.CONFIRM.value },
                cancelled = order.filter { it.status == Status.CANCELLED.value },
                messaging = order.filter { it.status == Status.MESSAGING.value },
                empty = listOf(
                    Order(
                        0.0,
                        0,
                        0,
                        0,
                        "unknown",
                        Traveler.empty(),
                        LastMessage.empty(),
                        0,
                        Reject.empty()
                    )
                )
            )
    }
}

data class Experience(
    val duration: Double,
    val id: Int,
    val price: Price,
    val title: String,
    val type: String,
    val coverImage: String
) {
    companion object {
        fun from(experience: ExperienceModel): Experience =
            with(experience) {
                Experience(
                    duration = duration ?: 0.0,
                    id = id ?: 0,
                    price = price?.let { Price.from(it) } ?: Price.emptyItem(),
                    title = title ?: "",
                    type = type ?: "",
                    coverImage = cover_image ?: ""
                )
            }

        fun emptyItem() = Experience(0.0, 0, Price.emptyItem(), "", "", "")
    }

}

@Parcelize
data class LastMessage(
    var comment: String,
    val submitDate: String,
    val userId: Int
) : Parcelable {
    companion object {
        fun from(lastMessage: LastMessageModel?): LastMessage =
            with(lastMessage) {
                LastMessage(
                    comment = this?.comment ?: "",
                    submitDate = this?.submit_date ?: "",
                    userId = this?.user_id ?: 0
                )
            }

        fun empty(): LastMessage =
            LastMessage(
                comment = "",
                submitDate = "",
                userId = 0
            )

    }
}

@Parcelize
data class Order(
    val fullPrice: Double,
    val id: Int,
    val offsitePayment: Int,
    val personsCount: Int,
    val status: String,
    val traveler: Traveler,
    val lastMessage: LastMessage,
    val newMessagesCount: Int,
    val reject: Reject,
    val currency: String? = null
) : Parcelable {
    companion object {
        fun from(order: OrderModel): Order =
            with(order) {
                Order(
                    fullPrice = full_price ?: 0.0,
                    id = id ?: 0,
                    offsitePayment = offsite_payment ?: 0,
                    personsCount = persons_count ?: 0,
                    status = status ?: "",
                    traveler = Traveler.from(traveler),
                    lastMessage = LastMessage.from(last_message),
                    newMessagesCount = new_messages_count ?: 0,
                    reject = Reject.from(reject)
                )
            }
    }

}

@Parcelize
data class Traveler(
    val avatar150Url: String,
    val avatar30Url: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String
) : Parcelable {
    companion object {
        fun from(traveler: TravelerModel?): Traveler =
            with(traveler) {
                Traveler(
                    avatar150Url = this?.avatar150_url ?: "",
                    avatar30Url = this?.avatar30_url ?: "",
                    email = this?.email ?: "",
                    id = this?.id ?: 0,
                    name = this?.name ?: "",
                    phone = this?.phone ?: ""
                )
            }

        fun empty(): Traveler =
            Traveler(
                avatar150Url = "",
                avatar30Url = "",
                email = "",
                id = 0,
                name = "",
                phone = ""
            )
    }
}

@Parcelize
data class Price(
    val currency: String,
    val currencyRate: Int,
    val priceFrom: Boolean,
    val unitString: String,
    val value: Double,
    val valueString: String
) : Parcelable {
    companion object {
        fun from(price: PriceModel): Price =
            with(price) {
                Price(
                    currency = currency ?: "",
                    currencyRate = currency_rate ?: 0,
                    priceFrom = price_from ?: false,
                    unitString = unit_string ?: "",
                    value = value ?: 0.0,
                    valueString = value_string ?: ""
                )
            }

        fun emptyItem() = Price("", 0, false, "", 0.0, "")
    }
}

@Parcelize
data class OrderDetailsGroup(
    val status: String?,
    val title: String?,
    val orders: SortedOrders?,
    val maxPersons: Int?,
    val numberOfPersonsAvail: Int?,
    val date: String?,
    val currency: String?,
    val type: String?,
    val duration: Double?,
    val awareStartDt: String?,
    val numberOfPersonsPaid: Int,
    val coverImage: String
) : Parcelable


@Parcelize
data class OrderDetailsForChat(
    val orderId: Int,
    val status: String,
    val title: String,
    val type: String,
    val date: String,
    val awareStartDt: Boolean,
    val phone: String,
    val name: String,
    val avatar: String,
    val memberCountAndPrice: String,
    val currency: String,
    val viewedByGuid: Boolean
) : Parcelable

@Parcelize
data class StatisticsData(
    val ordersRateValue: Int,
    val bookingRateValue: Int,
    val confirmRateValue: Int,
    val canOpenContacts: Boolean
) : Parcelable {
    companion object {

        fun from(orderStatisticsData: OrderStatisticsData): StatisticsData =
            with(orderStatisticsData) {
                StatisticsData(
                    ordersRateValue = (this.ordersRate.value * 100).toInt(),
                    bookingRateValue = (this.bookingRate.value * 100).toInt(),
                    confirmRateValue = (this.confirmationRate.value * 100).toInt(),
                    canOpenContacts = this.canOpenTravelerContacts
                )
            }

        fun empty(): StatisticsData = StatisticsData(
            0, 0, 0, false
        )
    }
}

data class IndividualOrder(
    val status: String, val awareStartDt: String,
    val orderId: Int, val eventId: Int, val type: String, val initiator: String
)

@Parcelize
data class CalendarSelectedData(
    val firstDateDay: LocalDate?,
    val secondDateDay: LocalDate?
) : Parcelable