package ru.tripster.entities.response.experience

data class PerPersonResponse(
    val id: Long?,
    val title: String?,
    val is_default: Boolean?,
    val value: Double?,
    val without_discount: Int?
)
