package ru.tripster.domain.model.order.orderrejectinfo

import ru.tripster.domain.model.order.orderrejectinfo.ApplicableReason.Companion.from
import ru.tripster.domain.model.order.orderrejectinfo.DuplicateOrder.Companion.from
import ru.tripster.entities.response.orders.ordersreject.OrderRejectResponse

data class RejectInfo(
    val applicableReasons: List<ApplicableReason>,
    val busyDays: ArrayList<String>,
    val calcPriceFrom: Int,
    val calcPriceTo: Int,
    val duplicateOrders: List<DuplicateOrder>
) {
    companion object {
        fun OrderRejectResponse.from() = RejectInfo(
            applicableReasons = applicableReasons?.map { it.from() } ?: emptyList(),
            busyDays = (busyDays ?: arrayListOf()) as ArrayList<String>,
            calcPriceFrom = calcPriceFrom ?: 0,
            calcPriceTo = calcPriceTo ?: 0,
            duplicateOrders = duplicateOrders?.map { it.from() } ?: emptyList()
        )
    }
}