package ru.tripster.domain.interactors.confirmOrEdit

import ru.tripster.domain.model.order.PerTicketOrderDetail
import ru.tripster.domain.model.order.TotalPriceAndPercent

interface CountTotalPriceUseCase {
    fun countTotalPrice(
        list:List<PerTicketOrderDetail>,
        totalPriceValue : Int
    ): TotalPriceAndPercent
}