package com.core.network.rest.response.model

import com.google.gson.annotations.SerializedName

data class BaseResponseData<out D : Any>(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("data") val data: D,
    @SerializedName("errors") val errors: List<ErrorResponseData>
)
