package ru.tripster.domain.interactors.confirmOrEdit

import ru.tripster.domain.model.order.*

interface GetAdapterListModelUseCase {
    fun getAdapterListModel(
        perTicket: List<PerTicketOrderDetail>,
        ticketDefinitions: List<PerPersonExperience>?,
        maxPerson: Int,
        currency: String,
        discountRate: Double,
        hasCustomPrice: Boolean,
        pricingModel: String,
        status: String
    ): Pair<List<PerTicketOrderDetail>, Int>
}