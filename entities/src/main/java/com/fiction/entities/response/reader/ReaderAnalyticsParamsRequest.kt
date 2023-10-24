package com.fiction.entities.response.reader

import com.google.gson.annotations.SerializedName

data class ReaderAnalyticsParamsRequest(
    @SerializedName("book_id")
    val bookId: Long?,
    @SerializedName("session_id")
    val sessionId: String?,
    @SerializedName("udid")
    val udid: String?,
)
