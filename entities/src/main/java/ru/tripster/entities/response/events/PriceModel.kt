package ru.tripster.entities.response.events

data class PriceModel(
    val currency: String?,
    val currency_rate: Int?,
    val price_from: Boolean?,
    val unit_string: String?,
    val value: Double?,
    val value_string: String?
)