package com.fiction.entities.response.reader

import com.google.gson.annotations.SerializedName

data class ReportParamsRequest(
    @SerializedName("percent")
    val percent: Float?,
    @SerializedName("report_type")
    val reportType: Int?,
    @SerializedName("other_text")
    val otherText: String?
)