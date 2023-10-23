package ru.tripster.entities.response


import com.google.gson.annotations.SerializedName

data class BaseResultModel<T>(
    @SerializedName("data")
    val data: T?,
    @SerializedName("success")
    val success: Boolean,

)