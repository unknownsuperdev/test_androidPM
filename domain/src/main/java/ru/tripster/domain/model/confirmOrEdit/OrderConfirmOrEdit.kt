package ru.tripster.domain.model.confirmOrEdit

import ru.tripster.domain.model.confirmOrEdit.Ticket.Companion.from
import ru.tripster.entities.request.confirm.OrderConfirmRequest
import ru.tripster.entities.request.confirm.TicketRequest
import ru.tripster.entities.request.chat.TravelerContactsOpenRequest

data class OrderConfirmOrEditData(
    val autoSetDayBusy: Boolean,
    val customPrice: Int?,
    val dateExact: String,
    val email: String,
    val meetingPoint: String,
    val personsCount: Int?,
    val phone: String,
    val tickets: List<Ticket>?,
    val timeStart: String,
    val travelerContactsOpen: Boolean
) {
    companion object {
        fun OrderConfirmOrEditData.from(): OrderConfirmRequest =
            OrderConfirmRequest(
                auto_set_day_busy = autoSetDayBusy,
                custom_price = customPrice,
                date_exact = dateExact,
                email = email,
                meeting_point = meetingPoint,
                persons_count = personsCount,
                phone = phone,
                tickets = tickets?.map { it.from() },
                time_start = timeStart,
                traveler_contacts_open = travelerContactsOpen
            )
    }
}

data class Ticket(
    val count: Int,
    val id: Long
) {
    companion object {
        fun Ticket.from(): TicketRequest = TicketRequest(count, id)
    }
}

data class EditTravelerContactsOpen(
    val travelerContactsOpen: Boolean
) {
    companion object {
        fun EditTravelerContactsOpen.from(): TravelerContactsOpenRequest =
            TravelerContactsOpenRequest(
                traveler_contacts_open = travelerContactsOpen
            )
    }
}




