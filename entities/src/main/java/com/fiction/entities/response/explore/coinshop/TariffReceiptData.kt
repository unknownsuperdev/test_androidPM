package com.fiction.entities.response.explore.coinshop

import com.google.gson.annotations.SerializedName

data class TariffReceiptData(
    @SerializedName("item")
    val item: String?,
    @SerializedName("receipt-data")
    val receiptData: String?
)
