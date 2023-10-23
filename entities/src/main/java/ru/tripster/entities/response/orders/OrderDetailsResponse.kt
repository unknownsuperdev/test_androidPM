package ru.tripster.entities.response.orders

import com.google.gson.annotations.SerializedName
import ru.tripster.entities.response.response.orders.TicketDefinitionsResponse

data class OrderDetailsResponse(
    @SerializedName("currency")
    val currency: Int? = null,
    @SerializedName("event")
    val event: EventOrderDetailResponse? = null,
    @SerializedName("experience")
    val experience: ExperienceOrderDetailResponse? = null,
    @SerializedName("guide_last_visit_date")
    val guide_last_visit_date: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("last_message")
    val last_message: LastMessageOrderDetailResponse? = null,
    @SerializedName("new_messages_count")
    val new_messages_count: Int? = null,
    @SerializedName("persons_count")
    val persons_count: Int? = null,
    @SerializedName("price")
    val price: PriceOrderDetailResponse? = null,
    @SerializedName("reject")
    val reject: RejectResponse? = null,
    @SerializedName("status")
    val status: String? = null,
    @SerializedName("traveler")
    val traveler: TravelerOrderDetailResponse? = null,
    @SerializedName("discount_rate")
    val discount_rate: Double? = null,
    @SerializedName("traveler_contacts_open")
    val traveler_contacts_open: Boolean? = null,
    @SerializedName("has_custom_price")
    val has_custom_price: Boolean? = null,
    @SerializedName("ticket_definitions")
    val ticket_definitions: List<TicketDefinitionsResponse>? = null,
    @SerializedName("last_modified_date")
    val last_modified_date: String? = null
)