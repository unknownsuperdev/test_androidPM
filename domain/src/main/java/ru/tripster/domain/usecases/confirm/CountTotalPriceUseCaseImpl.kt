package ru.tripster.domain.usecases.confirm

import ru.tripster.domain.interactors.confirmOrEdit.CountTotalPriceUseCase
import ru.tripster.domain.model.order.PerTicketOrderDetail
import ru.tripster.domain.model.order.TotalPriceAndPercent

class CountTotalPriceUseCaseImpl : CountTotalPriceUseCase {

    override fun countTotalPrice(
        list: List<PerTicketOrderDetail>,
        totalPriceValue: Int
    ): TotalPriceAndPercent {
        var totalPrice = 0

        list.forEach {
            if (it.pricingModel == "per_group" || it.hasCustomPrice) {
                totalPrice = it.price.toInt()
//                it.isNotChanged = true
            } else {
                totalPrice += it.oneTicketPrice * it.count
//                it.isNotChanged = false
            }
        }

//        if (totalPrice < totalPriceValue) {
//            totalPrice = totalPriceValue
//        }

        val forYou = totalPrice.minus(totalPrice * 23 / 100)
        val onApp = totalPrice.minus(forYou)

        return TotalPriceAndPercent(totalPrice, onApp, forYou)
    }

}