package ru.tripster.domain.model.order.orderrejectinfo

import ru.tripster.entities.response.orders.ordersreject.ApplicableReasonResponse

data class ApplicableReason(
    val key: Int,
    val latinKey: String,
    val verboseName: String
){
    companion object{
        fun ApplicableReasonResponse.from() = ApplicableReason(
            key = key ?: 0,
            latinKey = latinKey ?: "",
            verboseName = verboseName ?: ""
        )
    }
}