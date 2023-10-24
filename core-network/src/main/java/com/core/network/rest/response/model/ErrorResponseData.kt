package com.core.network.rest.response.model

import com.google.gson.annotations.SerializedName

data class ErrorResponseData(
    @SerializedName("name") var name: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("code") var code: Int? = null,
    @SerializedName("status") var status: Int? = null
)
