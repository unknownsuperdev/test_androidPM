package ru.tripster.domain.usecases.confirm

import ru.tripster.domain.interactors.confirmOrEdit.TicketsListIdCountUseCase
import ru.tripster.domain.model.order.PerTicketOrderDetail
import ru.tripster.domain.model.order.TicketType

class TicketsListIdCountUseCaseImpl : TicketsListIdCountUseCase {

    override fun ticketsListIdCount(ticketsList: List<PerTicketOrderDetail>): Pair<List<TicketType>, Int> {

        val newList: MutableList<TicketType> = mutableListOf()
        var totalCount = 0

        ticketsList.forEach {
            if (it.count != 0) newList.add(TicketType(it.id, it.count))
            totalCount += it.count
        }

        return Pair(newList, totalCount)
    }
}