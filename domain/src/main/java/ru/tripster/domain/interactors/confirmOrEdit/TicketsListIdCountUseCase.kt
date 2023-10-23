package ru.tripster.domain.interactors.confirmOrEdit

import ru.tripster.domain.model.order.PerTicketOrderDetail
import ru.tripster.domain.model.order.TicketType

interface TicketsListIdCountUseCase {
    fun ticketsListIdCount(ticketsList: List<PerTicketOrderDetail>): Pair<List<TicketType>, Int>
}