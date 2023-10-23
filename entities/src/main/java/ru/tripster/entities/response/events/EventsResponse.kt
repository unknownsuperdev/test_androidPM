package ru.tripster.entities.response.events

import com.google.gson.annotations.SerializedName

data class EventsResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<ResultModel>?
)