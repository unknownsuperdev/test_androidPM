package ru.tripster.domain.model.order.orderrejectinfo

import ru.tripster.entities.response.orders.ordersreject.DuplicateOrderResponse

data class DuplicateOrder(
    val date: String,
    val experienceId: Int,
    val experienceTitle: String,
    val expType: String,
    val id: Int,
    val time: String,
    val travelerName: String
) {
    companion object {
        fun DuplicateOrderResponse.from() = DuplicateOrder(
            date = date ?: "",
            experienceId = experienceId ?: 0,
            expType = expType ?: "",
            experienceTitle = experienceTitle ?: "",
            id = id ?: 0,
            time = time ?: "",
            travelerName = travelerName ?: ""
        )
    }
}