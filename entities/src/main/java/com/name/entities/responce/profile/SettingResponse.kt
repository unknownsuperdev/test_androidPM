package com.name.entities.responce.profile


import com.google.gson.annotations.SerializedName

data class SettingResponse(
    @SerializedName("auto_unlock_paid")
    val autoUnlockPaid: Boolean?,
    @SerializedName("reading_mode")
    val readingMode: Boolean?
)