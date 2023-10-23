package ru.tripster.entities.response.calendar.filtering

import com.google.gson.annotations.SerializedName

data class ExperienceTitleResponseModel(
    @SerializedName("format")
    val format: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("is_visible")
    val is_visible: Boolean?,
    @SerializedName("movement_type")
    val movement_type: String?,
    @SerializedName("tagline")
    val tagline: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?
)