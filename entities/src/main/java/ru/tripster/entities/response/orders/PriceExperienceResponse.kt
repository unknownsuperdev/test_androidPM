package ru.tripster.entities.response.orders

data class PriceExperienceResponse(
    val currency: String?,
    val currency_rate: Double?,
    val discount: DiscountResponse?,
    val onsite_payment: Double?,
    val per_person: List<PerPersonExperienceResponse>?,
    val per_group: PerGroupResponse?,
    val price_description: String?,
    val total_commission_rate: Int?,
    val unit_string: String?,
    val value_string: String?
)
