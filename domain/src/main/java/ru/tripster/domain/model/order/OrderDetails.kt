package ru.tripster.domain.model.order

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.tripster.core.DiffUtilModel
import ru.tripster.domain.model.order.ChangeExperience.Companion.from
import ru.tripster.entities.request.confirm.TicketRequest
import ru.tripster.entities.response.experience.ChangeExperienceResponse
import ru.tripster.entities.response.experience.PerPersonResponse
import ru.tripster.entities.response.orders.*
import ru.tripster.entities.response.response.orders.TicketDefinitionsResponse

@Parcelize
data class OrderDetails(
    val currency: Int,
    val event: EventOrderDetail,
    val experience: ExperienceOrderDetail,
    val guideLastVisitDate: String,
    val id: Int,
    val lastMessage: LastMessageOrderDetail,
    val newMessagesCount: Int,
    val personsCount: Int,
    val price: PriceOrderDetail,
    val reject: Reject,
    val status: String,
    val traveler: TravelerOrderDetail,
    val discountRate: Double,
    val travelerContactsOpen: Boolean,
    val hasCustomPrice: Boolean,
    val ticketDefinitions: List<TicketDefinitions>,
    val lastModifiedDate: String,
    var isConfirm: Boolean = false
) : Parcelable {
    companion object {
        fun OrderDetailsResponse.from(): OrderDetails =
            OrderDetails(
                currency = currency ?: 0,
                event = EventOrderDetail.from(event),
                experience = ExperienceOrderDetail.from(experience),
                guideLastVisitDate = guide_last_visit_date ?: "",
                id = id ?: 0,
                lastMessage = LastMessageOrderDetail.from(last_message),
                newMessagesCount = new_messages_count ?: 0,
                personsCount = persons_count ?: 0,
                price = PriceOrderDetail.from(price),
                reject = Reject.from(reject),
                status = status ?: "",
                traveler = TravelerOrderDetail.from(traveler),
                discountRate = discount_rate ?: 0.0,
                travelerContactsOpen = traveler_contacts_open ?: false,
                hasCustomPrice = has_custom_price ?: false,
                ticketDefinitions = ticket_definitions?.map { TicketDefinitions.from(it) }
                    ?: emptyList(),
                lastModifiedDate = last_modified_date ?: ""
            )
    }
}

@Parcelize
data class ChangeExperience(
    val currency: String,
    val currencyRate: Double,
    val paymentToGuide: Double,
    val perTicket: List<PerTicketOrderDetail>,
    val prePay: Double,
    val priceDescription: String,
    val value: Double,
    val valueString: String,
    val perPerson: List<PerPerson>
) : Parcelable {
    companion object {
        fun ChangeExperienceResponse.from(): ChangeExperience =
            ChangeExperience(
                currency = currency ?: "",
                currencyRate = currency_rate ?: 0.0,
                paymentToGuide = payment_to_guide ?: 0.0,
                perTicket = per_ticket?.map { PerTicketOrderDetail.from(it) } ?: emptyList(),
                prePay = pre_pay ?: 0.0,
                priceDescription = price_description ?: "",
                value = value ?: 0.0,
                valueString = value_string ?: "",
                perPerson = per_person?.map { PerPerson.from(it) } ?: emptyList()
            )

        fun empty(): ChangeExperience =
            ChangeExperience(
                currency = "",
                currencyRate = 0.0,
                paymentToGuide = 0.0,
                perTicket = emptyList(),
                prePay = 0.0,
                priceDescription = "",
                value = 0.0,
                valueString = "",
                perPerson = emptyList()
            )
    }
}

@Parcelize
data class PerPerson(
    val id: Long,
    val title: String,
    val isDefault: Boolean,
    val value: Double,
    val withoutDiscount: Int
) : Parcelable {
    companion object {
        fun from(perPerson: PerPersonResponse): PerPerson =
            with(perPerson) {
                PerPerson(
                    id = id ?: 0,
                    title = title ?: "",
                    isDefault = is_default ?: false,
                    value = value ?: 0.0,
                    withoutDiscount = without_discount ?: 0
                )
            }
    }
}
@Parcelize
data class EventOrderDetail(
    val date: String,
    val isForGroups: Boolean,
    val maxPersons: Int,
    val meetingPoint: MeetingPoint,
    val numberOfPersonsAvail: Int,
    val time: String,
    val awareStartDt: String,
    val email: String,
    val phone: String
) : Parcelable {
    companion object {
        fun from(eventOrderDetail: EventOrderDetailResponse?): EventOrderDetail =
            with(eventOrderDetail) {
                EventOrderDetail(
                    this?.date ?: "",
                    this?.is_for_groups ?: false,
                    this?.max_persons ?: 0,
                    MeetingPoint.from(this?.meeting_point),
                    this?.number_of_persons_avail ?: 0,
                    this?.time ?: "",
                    this?.aware_start_dt ?: "",
                    this?.email ?: "",
                    this?.phone ?: "",
                )
            }
    }
}

@Parcelize
data class MeetingPoint(
    val description: String,
    val location: Location
) : Parcelable {
    companion object {
        fun from(meetingPoint: MeetingPointResponse?): MeetingPoint =
            with(meetingPoint) {
                MeetingPoint(
                    this?.description ?: "", Location.from(this?.location)
                )
            }
    }
}

@Parcelize
data class Location(
    val lat: Double,
    val lng: Double
) : Parcelable {
    companion object {
        fun from(location: LocationResponse?): Location =
            Location(
                location?.lat ?: 0.0, location?.lng ?: 0.0
            )
    }
}

@Parcelize
data class ExperienceOrderDetail(
    val id: Int,
    val title: String,
    val duration: Double,
    val coverImage: String,
    val pricingModel: String,
    val price: PriceExperience
) : Parcelable {
    companion object {
        fun from(experienceOrderDetail: ExperienceOrderDetailResponse?): ExperienceOrderDetail =
            with(experienceOrderDetail) {
                ExperienceOrderDetail(
                    this?.id ?: 0,
                    this?.title ?: "",
                    this?.duration ?: 0.0,
                    this?.coverImage ?: "",
                    this?.pricing_model ?: "",
                    PriceExperience.from(this?.price)
                )
            }
    }
}

@Parcelize
data class PriceExperience(
    val currency: String,
    val currencyRate: Double,
    val discount: Discount,
    val onsitePayment: Double,
    val perPerson: List<PerPersonExperience>?,
    val perGroup: PerGroup?,
    val priceDescription: String,
    val totalCommissionRate: Int,
    val unitString: String,
    val valueString: String
) : Parcelable {
    companion object {
        fun from(priceExperience: PriceExperienceResponse?): PriceExperience =
            with(priceExperience) {
                PriceExperience(
                    currency = this?.currency ?: "",
                    currencyRate = this?.currency_rate ?: 0.0,
                    discount = Discount.from(this?.discount),
                    onsitePayment = this?.onsite_payment ?: 0.0,
                    perPerson = this?.per_person?.map { PerPersonExperience.from(it) }
                        ?: emptyList(),
                    perGroup = PerGroup.from(this?.per_group),
                    priceDescription = this?.price_description ?: "",
                    totalCommissionRate = this?.total_commission_rate ?: 0,
                    unitString = this?.unit_string ?: "",
                    valueString = this?.value_string ?: ""
                )
            }

        fun empty(): PriceExperience =
            PriceExperience(
                currency = "",
                currencyRate = 0.0,
                discount = Discount.empty(),
                onsitePayment = 0.0,
                perPerson = emptyList(),
                perGroup = PerGroup.empty(),
                priceDescription = "",
                totalCommissionRate = 0,
                unitString = "",
                valueString = ""
            )
    }
}

@Parcelize
data class PerGroup(val value: Double) : Parcelable {
    companion object {
        fun from (perGroup: PerGroupResponse?): PerGroup = with(perGroup) {
            PerGroup(this?.value ?: 0.0)
        }
        fun empty() : PerGroup = PerGroup(
            0.0
        )
    }
}

@Parcelize
data class Discount(
    val expiration: String,
    val value: Double
) : Parcelable {
    companion object {
        fun from(discount: DiscountResponse?): Discount = with(discount) {
            Discount(
                expiration = this?.expiration ?: "",
                value = this?.value ?: 0.0
            )
        }

        fun empty(): Discount =
            Discount(
                expiration = "",
                value = 0.0
            )

    }
}

@Parcelize
data class PerPersonExperience(
    val id: Long,
    val isDefault: Boolean,
    val title: String,
    val value: Double,
    val withoutDiscount: Int
) : Parcelable {
    companion object {
        fun from(perPersonExperience: PerPersonExperienceResponse?): PerPersonExperience =
            with(perPersonExperience) {
                PerPersonExperience(
                    id = this?.id ?: 0,
                    isDefault = this?.is_default ?: false,
                    title = this?.title ?: "",
                    value = this?.value ?: 0.0,
                    withoutDiscount = this?.without_discount ?: 0
                )
            }
    }
}

@Parcelize
data class LastMessageOrderDetail(
    val comment: String,
    val submitDate: String,
    val userId: Int,
    val viewedByGuide: Boolean
) : Parcelable {
    companion object {
        fun from(lastMessageOrderDetail: LastMessageOrderDetailResponse?): LastMessageOrderDetail =
            with(lastMessageOrderDetail) {
                LastMessageOrderDetail(
                    this?.comment ?: "",
                    this?.submit_date ?: "",
                    this?.user_id ?: 0,
                    this?.viewed_by_guide ?: false
                )
            }
    }
}

@Parcelize
data class PriceOrderDetail(
    val currency: String,
    val currencyRate: Int,
    val paymentToGuide: Double,
    val perPicket: List<PerTicketOrderDetail>,
    val prePay: Double,
    val priceDescription: String,
    val value: Double,
    val valueString: String
) : Parcelable {
    companion object {
        fun from(priceOrderDetail: PriceOrderDetailResponse?): PriceOrderDetail =
            with(priceOrderDetail) {
                PriceOrderDetail(
                    this?.currency ?: "",
                    this?.currency_rate ?: 0,
                    this?.payment_to_guide ?: 0.0,
                    this?.per_ticket?.map { PerTicketOrderDetail.from(it) } ?: emptyList(),
                    this?.pre_pay ?: 0.0,
                    this?.price_description ?: "",
                    this?.value ?: 0.0,
                    this?.value_string ?: ""
                )
            }
    }
}

@Parcelize
data class PerTicket(
    val count: Int,
    override val id: Long,
    var price: Double,
    val title: String,
    val totalPrice: Double? = null,
) : Parcelable, DiffUtilModel<Long>() {
    companion object {
        fun from(perTicketOrderDetail: PerTicketOrderDetailResponse): PerTicket =
            with(perTicketOrderDetail) {
                PerTicket(
                    count ?: 0,
                    id ?: 0L,
                    price ?: 0.0,
                    title ?: ""
                )
            }
    }
}

@Parcelize
data class TicketType(
    val id: Long,
    var count: Int
) : Parcelable {
    companion object {
        fun from(ticket: TicketRequest): TicketType =
            with(ticket) {
                TicketType(
                    id ?: 0L,
                    count ?: 0
                )
            }
    }
}

@Parcelize
data class PerTicketOrderDetail(
    val count: Int,
    override val id: Long,
    val price: Double,
    val title: String,
    val currency: String? = null,
    val hasCustomPrice: Boolean = false,
    val totalCountIsMax: Boolean = false,
    val maxPersonCount: Int = 0,
    val oneTicketPrice: Int = 0,
    val pricingModel: String? = null,
    val status: String? = null
) : Parcelable, DiffUtilModel<Long>() {
    companion object {
        fun from(perTicketOrderDetail: PerTicketOrderDetailResponse): PerTicketOrderDetail =
            with(perTicketOrderDetail) {
                PerTicketOrderDetail(
                    count ?: 0,
                    id ?: 0L,
                    price ?: 0.0,
                    title ?: ""
                )
            }
    }
}

@Parcelize
data class TravelerOrderDetail(
    val avatar150Url: String,
    val avatar30Url: String,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String
) : Parcelable {
    companion object {
        fun from(travelerOrderDetail: TravelerOrderDetailResponse?): TravelerOrderDetail =
            with(travelerOrderDetail) {
                TravelerOrderDetail(
                    this?.avatar150_url ?: "",
                    this?.avatar30_url ?: "",
                    this?.email ?: "",
                    this?.id ?: 0,
                    this?.name ?: "",
                    this?.phone ?: ""
                )
            }
    }
}

@Parcelize
data class TicketDefinitions(
    val id: Long,
    val title: String,
    val price: Double
) : Parcelable {
    companion object {
        fun from(reject: TicketDefinitionsResponse?): TicketDefinitions =
            with(reject) {
                TicketDefinitions(
                    this?.id ?: 0,
                    this?.title ?: "",
                    this?.price ?: 0.0
                )
            }

        fun empty(): TicketDefinitions =
            TicketDefinitions(
                id = 0,
                title = "",
                price = 0.0
            )
    }
}


@Parcelize
data class Reject(
    val reason: String,
    val reason_text: String,
    val message: String,
    val initiator: String,
    val date: String
) : Parcelable {
    companion object {
        fun from(reject: RejectResponse?): Reject =
            with(reject) {
                Reject(
                    this?.reason ?: "",
                    this?.reason_text ?: "",
                    this?.message ?: "",
                    this?.initiator ?: "",
                    this?.date ?: ""
                )
            }

        fun empty(): Reject =
            Reject(
                reason = "",
                reason_text = "",
                message = "",
                initiator = "",
                date = ""
            )
    }
}

@Parcelize
data class DetailsForConfirm(
    val date: String,
    val time: String,
    val email: String,
    val phone: String,
    val tickets: List<PerTicketOrderDetail>? = null,
    val currency: String,
    val meetingPoint: String? = null,
    val maxPerson: Int,
    val title: String,
    val number: Int,
    val type: String
) : Parcelable

@Parcelize
data class ChooseTime(
    override val id: Int,
    val time: String,
    val isChecked: Boolean = false,
    val isValid: Boolean? = null
) : Parcelable, DiffUtilModel<Int>()


data class TotalPriceAndPercent(
    val totalPrice: Int,
    val onApp: Int,
    val forYou: Int
)