package ru.tripster.entities.request.chat

import com.google.gson.annotations.SerializedName

data class OrderCommentViewedRequest(
    @SerializedName("viewed_by_guide")
    val viewed_by_guide: Boolean?
)
