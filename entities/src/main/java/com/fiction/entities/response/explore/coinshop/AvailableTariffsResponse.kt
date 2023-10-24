package com.fiction.entities.response.explore.coinshop

import com.google.gson.annotations.SerializedName

data class AvailableTariffsResponse(
    @SerializedName("balance")
    val balance: Long?,
    @SerializedName("items")
    val tariffs: List<TariffResponse>?
)