package ru.tripster.entities.response.calendar.dateOrder

import ru.tripster.entities.response.events.PriceModel

data class ExperienceResponseModel(
    val cover_image: String?,
    val duration: Double?,
    val id: Int?,
    val price: PriceModel?,
    val title: String?,
    val type: String?
)