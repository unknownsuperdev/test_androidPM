package ru.tripster.domain.usecases.confirm

import ru.tripster.domain.interactors.confirmOrEdit.GetAdapterListModelUseCase
import ru.tripster.domain.model.order.*

class GetAdapterListModelUseCaseImpl : GetAdapterListModelUseCase {
    override fun getAdapterListModel(
        perTicket: List<PerTicketOrderDetail>,
        ticketDefinitions: List<PerPersonExperience>?,
        maxPerson: Int,
        currency: String,
        discountRate: Double,
        hasCustomPrice: Boolean,
        pricingModel: String,
        status: String
    ): Pair<List<PerTicketOrderDetail>, Int> {

        val perTicketPrice: MutableList<PerTicketOrderDetail> = mutableListOf()
        var totalCount = 0

        if (!ticketDefinitions.isNullOrEmpty()) {
            ticketDefinitions.forEach {
                val ticket = perTicket.find { perTicket -> perTicket.id == it.id }

                perTicketPrice.add(
                    PerTicketOrderDetail(
                        count = ticket?.count ?: 0,
                        id = ticket?.id ?: it.id,
                        price = ticket?.price ?: it.value,
                        title = ticket?.title ?: it.title,
                        currency = currency,
                        pricingModel = pricingModel,
                        hasCustomPrice = hasCustomPrice,
                        oneTicketPrice = it.value.minus(it.value * discountRate).toInt(),
                        status = status
                    )
                )
            }
        } else {
            perTicketPrice.addAll(perTicket)

            perTicketPrice[0] = perTicketPrice[0].copy(
                currency = currency,
                oneTicketPrice = 0,
                hasCustomPrice = hasCustomPrice,
                pricingModel = pricingModel,
                status = status
            )
        }

        perTicketPrice.forEach { totalCount += it.count }

        if (totalCount >= maxPerson) perTicketPrice.apply {
            this.forEachIndexed { index, perTicketOrderDetail ->
                this[index] = perTicketOrderDetail.copy(totalCountIsMax = true, status = status)
            }
        }

        return Pair(perTicketPrice, totalCount)
    }
}