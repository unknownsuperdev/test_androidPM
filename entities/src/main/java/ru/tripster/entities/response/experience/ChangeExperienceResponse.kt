package ru.tripster.entities.response.experience

import com.google.gson.annotations.SerializedName
import ru.tripster.entities.response.orders.PerTicketOrderDetailResponse

data class ChangeExperienceResponse(
    @SerializedName("currency")
    val currency: String?,
    @SerializedName("currency_rate")
    val currency_rate: Double?,
    @SerializedName("payment_to_guide")
    val payment_to_guide: Double?,
    @SerializedName("per_ticket")
    val per_ticket: List<PerTicketOrderDetailResponse>?,
    @SerializedName("pre_pay")
    val pre_pay: Double?,
    @SerializedName("price_description")
    val price_description: String?,
    @SerializedName("value")
    val value: Double?,
    @SerializedName("value_string")
    val value_string: String?,
    @SerializedName("per_person")
    val per_person: List<PerPersonResponse>?
)