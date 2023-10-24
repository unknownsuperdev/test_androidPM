package com.fiction.entities.response.gifts

import com.google.gson.annotations.SerializedName

data class EventIdsRequest(
    @SerializedName("ids")
    val ids: List<Int>?
)
