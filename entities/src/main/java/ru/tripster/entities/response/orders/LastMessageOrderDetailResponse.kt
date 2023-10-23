package ru.tripster.entities.response.orders

data class LastMessageOrderDetailResponse(
    val comment: String?,
    val submit_date: String?,
    val user_id: Int?,
    val viewed_by_guide: Boolean?
)