package ru.tripster.entities.response.orders

data class TravelerOrderDetailResponse(
    val avatar150_url: String?,
    val avatar30_url: String?,
    val email: String?,
    val id: Int?,
    val name: String?,
    val phone: String?
)