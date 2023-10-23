package ru.tripster.domain.model.calendar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.tripster.core.DiffUtilModel
import ru.tripster.domain.model.events.LastMessage
import ru.tripster.domain.model.events.Price
import ru.tripster.domain.model.events.StatisticsData
import ru.tripster.domain.model.events.Traveler
import ru.tripster.domain.model.order.Reject
import ru.tripster.entities.request.calendar.closeTime.CloseTimeRequest

import java.time.LocalDate
import ru.tripster.entities.response.calendar.GuidesScheduleResponse
import ru.tripster.entities.response.calendar.ScheduleResponse
import ru.tripster.entities.response.calendar.dateOrder.*

@Parcelize
data class GuidesSchedule(
    val date: String,
    val dayType: CalendarDayType,
    val guideSchedule: List<DateScheduleItem>
) : Parcelable {
    companion object {
        fun GuidesScheduleResponse.from(): GuidesSchedule =
            GuidesSchedule(
                date = date ?: "",
                dayType = guideScheduleResponses.getType(),
                guideSchedule = guideScheduleResponses?.map {
                    DateScheduleItem.fromSchedule(it, date ?: "")
                } ?: emptyList()
//                guideScheduleResponses?.let {
//                    guideScheduleResponses?.map {
//                        from(it)
//                    }
//                } ?: emptyList()
            )
    }
}

@Parcelize
data class CloseTimeSchedule(
    val comment: String?,
    val duration: Int,
    val endDate: String,
    val experience: Int?,
    val startDate: String,
    val startTime: String
) : Parcelable {
    companion object {
        fun CloseTimeSchedule.from(): CloseTimeRequest =
            CloseTimeRequest(
                comment = comment,
                duration = duration,
                end_date = endDate,
                experience = experience,
                start_time = startTime,
                start_date = startDate
            )
    }
}

data class GuideDateSchedule(
    val date: String,
    val eventCount: Int,
    val eventTotalCount: Int,
    val guideScheduleItem: List<DateScheduleItem>,
    var experienceId: Int = 0,
    var itemCount: Int = 0
) {
    companion object {
        fun GuideDateScheduleResponse.from(): GuideDateSchedule = GuideDateSchedule(
            date = date ?: "",
            eventCount = event_count ?: 0,
            eventTotalCount = event_total_count ?: 0,
            guideScheduleItem = guideScheduleItemResponses?.let {
                it.map { scheduleItem ->
                    DateScheduleItem.from(scheduleItem)
                }
            } ?: emptyList()
        )
    }
}

@Parcelize
data class DateScheduleItem(
    val duration: Int,
    val eventData: EventData,
    val experienceId: Int,
    val experienceName: String,
    val comment: String,
    override val id: Long,
    val time: String,
    val type: String,
    val date: String = ""
) : DiffUtilModel<Long>(), Parcelable {
    companion object {
        fun from(
            dateScheduleItemResponse: DateScheduleItemResponse?
        ): DateScheduleItem =
            with(dateScheduleItemResponse) {
                DateScheduleItem(
                    duration = this?.duration ?: 0,
                    eventData = EventData.from(this?.event_data),
                    experienceId = this?.experience_id ?: 0,
                    id = this?.id?.toLong() ?: 0,
                    time = this?.time ?: "",
                    type = this?.type ?: "",
                    comment = this?.comment ?: "",
                    experienceName = this?.experience_name ?: ""
                )
            }

        fun fromSchedule(
            dateScheduleItemResponse: ScheduleResponse?, date: String
        ): DateScheduleItem = with(dateScheduleItemResponse) {
            DateScheduleItem(
                duration = this?.duration ?: 0,
                eventData = EventData.from(this?.event_data),
                experienceId = this?.experience_id ?: 0,
                id = this?.id?.toLong() ?: 0,
                time = this?.time ?: "",
                type = this?.type ?: "",
                comment = "",
                date = date,
                experienceName = this?.experience_name ?: ""
            )
        }
    }
}

@Parcelize
data class EventData(
    val awareStartDt: String,
    val date: String,
    val experience: ScheduleDateExperience,
    val guideLastVisitDate: String,
    val id: Int,
    val isForGroups: Boolean,
    val maxPersons: Int,
    val numberOfPersonsOverall: Int,
    val numberOfPersonsPaid: Int,
    val orders: List<DateOrder>,
    val status: String,
    val time: String,
    val numberOfPersonsAvail: Int,
) : Parcelable {
    companion object {
        fun from(eventDataResponse: EventDataResponse?): EventData =
            with(eventDataResponse) {
                EventData(
                    awareStartDt = this?.aware_start_dt ?: "",
                    date = this?.date ?: "",
                    experience = ScheduleDateExperience.from(this?.experience),
                    guideLastVisitDate = this?.guide_last_visit_date ?: "",
                    id = this?.id ?: 0,
                    isForGroups = this?.is_for_groups ?: false,
                    maxPersons = this?.max_persons ?: 0,
                    numberOfPersonsOverall = this?.number_of_persons_overall ?: 0,
                    numberOfPersonsPaid = this?.number_of_persons_paid ?: 0,
                    orders = this?.orders?.let {
                        it.map { orderResponse ->
                            DateOrder.from(orderResponse)
                        }
                    } ?: emptyList(),
                    status = this?.status ?: "",
                    time = this?.time ?: "",
                    numberOfPersonsAvail = this?.number_of_persons_avail ?: 0,
                )
            }
    }
}


@Parcelize
data class DateOrder(
    val fullPrice: Int,
    val id: Int,
    val lastMessage: LastMessage,
    val newMessagesCount: Int,
    val personsCount: Int,
    val status: String,
    val traveler: Traveler,
    val offsitePayment: Int
) : Parcelable {
    companion object {
        fun from(orderResponse: OrderResponse): DateOrder =
            with(orderResponse) {
                DateOrder(
                    fullPrice = this.full_price ?: 0,
                    id = this.id ?: 0,
                    lastMessage = LastMessage.from(this.last_message),
                    newMessagesCount = this.new_messages_count ?: 0,
                    personsCount = this.persons_count ?: 0,
                    status = this.status ?: "",
                    traveler = Traveler.from(this.traveler),
                    offsitePayment = this.offsite_payment ?: 0
                )
            }
    }
}

@Parcelize
data class ScheduleDateExperience(
    val coverImage: String,
    val duration: Double,
    val id: Int,
    val price: Price,
    val title: String,
    val type: String
) : Parcelable {
    companion object {
        fun from(experienceResponseModel: ExperienceResponseModel?): ScheduleDateExperience =
            with(experienceResponseModel) {
                ScheduleDateExperience(
                    coverImage = this?.cover_image ?: "",
                    duration = this?.duration ?: 0.0,
                    id = this?.id ?: 0,
                    price = this?.price?.let { Price.from(it) } ?: Price.emptyItem(),
                    title = this?.title ?: "",
                    type = this?.type ?: ""
                )
            }
    }
}

data class SchedulePrice(
    val additionalProp1: String,
    val additionalProp2: String,
    val additionalProp3: String
) {
    companion object {
        fun from(priceResponse: PriceResponse?): SchedulePrice =
            with(priceResponse) {
                SchedulePrice(
                    additionalProp1 = this?.additionalProp1 ?: "",
                    additionalProp2 = this?.additionalProp2 ?: "",
                    additionalProp3 = this?.additionalProp3 ?: ""
                )
            }
    }
}

@Parcelize
data class SelectedIntervalData(
    val firstSelectedDate: LocalDate?,
    val secondSelectedDate: LocalDate?,
    val selectedDayDateType: CalendarDayType?
) : Parcelable

@Parcelize
data class DataForNav(
    val id: Int,
    val eventId: Int?,
    val type: String?,
    val isNavigatedDateOrder: Boolean,
    val experienceTitle: String? = null
) : Parcelable

@Parcelize
data class StartEndDate(
    val startDate: LocalDate,
    val endDate: LocalDate?,
) : Parcelable





