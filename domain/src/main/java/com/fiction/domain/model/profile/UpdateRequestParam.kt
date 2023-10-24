package com.fiction.domain.model.profile

import com.google.gson.annotations.SerializedName
import okhttp3.RequestBody

data class UpdateRequestParam(
    @SerializedName("name")
    val name: String?,
    @SerializedName("avatar")
    val avatar: RequestBody?,
    @SerializedName("auto_unlock_paid")
    val autoUnlockPaid: Boolean?,
    @SerializedName("reading_mode")
    val readingMode: Boolean?
)