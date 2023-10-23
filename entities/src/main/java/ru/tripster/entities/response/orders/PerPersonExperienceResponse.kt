package ru.tripster.entities.response.orders

data class PerPersonExperienceResponse(
    val id: Long?,
    val is_default: Boolean?,
    val title: String?,
    val value: Double?,
    val without_discount: Int?
)
