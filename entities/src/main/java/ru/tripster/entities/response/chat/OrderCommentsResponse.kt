package ru.tripster.entities.response.chat

import com.google.gson.annotations.SerializedName

data class OrderCommentsResponse(
    @SerializedName("count")
    val count: Int?,
    @SerializedName("next")
    val next: String?,
    @SerializedName("previous")
    val previous: String?,
    @SerializedName("results")
    val results: List<ResultResponse>?
)