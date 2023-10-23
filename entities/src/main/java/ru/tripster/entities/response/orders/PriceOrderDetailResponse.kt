package ru.tripster.entities.response.orders

data class PriceOrderDetailResponse(
    val currency: String?,
    val currency_rate: Int?,
    val payment_to_guide: Double?,
    val per_ticket: List<PerTicketOrderDetailResponse>?,
    val pre_pay: Double?,
    val price_description: String?,
    val value: Double?,
    val value_string: String?
)