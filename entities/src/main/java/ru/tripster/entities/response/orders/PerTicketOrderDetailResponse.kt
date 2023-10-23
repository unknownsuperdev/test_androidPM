package ru.tripster.entities.response.orders

data class PerTicketOrderDetailResponse(
    val count: Int?,
    val id: Long?,
    val price: Double?,
    val title: String?
)