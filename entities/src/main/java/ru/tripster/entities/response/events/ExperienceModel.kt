package ru.tripster.entities.response.events

data class ExperienceModel(
    val duration: Double?,
    val id: Int?,
    val price: PriceModel?,
    val title: String?,
    val type: String?,
    val cover_image:String?
)