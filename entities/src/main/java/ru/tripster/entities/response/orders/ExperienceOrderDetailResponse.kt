package ru.tripster.entities.response.orders

import com.google.gson.annotations.SerializedName

data class ExperienceOrderDetailResponse(
    val id: Int?,
    val title: String?,
    val duration: Double?,
    @SerializedName("cover_image")
    val coverImage : String?,
    @SerializedName("pricing_model")
    val pricing_model : String?,
    @SerializedName("price")
    val price : PriceExperienceResponse?
)