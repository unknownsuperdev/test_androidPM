package ru.tripster.entities.response.orders

data class RejectResponse(
    val reason: String?,
    val reason_text: String?,
    val message: String?,
    val initiator: String?,
    val date: String?
)