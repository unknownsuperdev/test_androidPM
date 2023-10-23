package ru.tripster.entities.responce.response.statistics

data class ConfirmationRateResponse(
    val excellent_value: Double?,
    val good_value: Double?,
    val is_bad: Boolean?,
    val is_excellent: Boolean?,
    val is_good: Boolean?,
    val is_poor: Boolean?,
    val is_undefined: Boolean?,
    val poor_value: Double?,
    val value: Double?
)