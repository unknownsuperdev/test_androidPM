package ru.tripster.domain.model.cancel

import ru.tripster.domain.model.cancel.Data.Companion.from
import ru.tripster.entities.request.cancel.DataRequest
import ru.tripster.entities.request.cancel.OrderCancelRequest

data class OrderCancelData(
    val key: Int,
    val message: String?,
    val data: Data
) {
    companion object {
        fun OrderCancelData.from(): OrderCancelRequest =
            OrderCancelRequest(
                key = key,
                message = message,
                data = data.from()
            )
    }
}

data class Data(
    val duplicateDate: String?,
    val duplicateOrder: Int?
) {
    companion object {
        fun Data.from(): DataRequest =
            DataRequest(
                duplicateDate, duplicateOrder
            )
    }
}
