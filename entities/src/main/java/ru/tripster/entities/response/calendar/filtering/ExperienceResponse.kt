package ru.tripster.entities.response.calendar.filtering

data class ExperienceResponse(
    val count: Int?,
    val next: String?,
    val previous: String?,
    val results: List<ExperienceTitleResponseModel>?
)