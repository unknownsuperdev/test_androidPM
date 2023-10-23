package ru.tripster.domain.model.order

import ru.tripster.entities.response.orders.OrderCounterResponse

data class OrderCounterModel(
    val needAttention: Int
) {
    companion object {
        fun from(counterResponse: OrderCounterResponse): OrderCounterModel =
            with(counterResponse) {
                OrderCounterModel(needAttention = need_attention ?: 0)
            }
    }
}
