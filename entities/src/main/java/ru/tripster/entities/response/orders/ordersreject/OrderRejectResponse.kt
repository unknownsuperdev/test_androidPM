package ru.tripster.entities.response.orders.ordersreject

import com.google.gson.annotations.SerializedName

data class OrderRejectResponse(
    @SerializedName("applicable_reasons")
    val applicableReasons: List<ApplicableReasonResponse>?,
    @SerializedName("busy_days")
    val busyDays: List<String>?,
    @SerializedName("calc_price_from")
    val calcPriceFrom: Int?,
    @SerializedName("calc_price_to")
    val calcPriceTo: Int?,
    @SerializedName("duplicate_orders")
    val duplicateOrders: List<DuplicateOrderResponse>?
)