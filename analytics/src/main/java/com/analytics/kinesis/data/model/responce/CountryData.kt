package com.analytics.kinesis.data.model.responce

import com.google.gson.annotations.SerializedName

internal data class CountryData(
    @SerializedName("country")
    val country: String?
)
