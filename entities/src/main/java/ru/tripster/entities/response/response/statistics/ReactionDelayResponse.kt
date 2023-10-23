package ru.tripster.entities.responce.response.statistics

data class ReactionDelayResponse(
    val excellent_value: Int?,
    val good_value: Int?,
    val is_bad: Boolean?,
    val is_excellent: Boolean?,
    val is_good: Boolean?,
    val is_poor: Boolean?,
    val is_undefined: Boolean?,
    val poor_value: Int?,
    val value: Int?
)