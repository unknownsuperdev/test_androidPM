package ru.tripster.entities.response

import com.google.gson.annotations.*

data class BaseDataModel(
    @SerializedName("memes")
    val memes: List<DataInfoResponse>
)
