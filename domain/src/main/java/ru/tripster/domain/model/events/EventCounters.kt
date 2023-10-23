package ru.tripster.domain.model.events

import ru.tripster.entities.response.events.EventCountersResponse

data class EventCounters(
    val booked: Int,
    val canceled: Int,
    val confirmation: Int,
    val finished: Int,
    val inWork: Int,
    val pendingPayment: Int,
    val unread: Int,
    val needAttention: Int
) {
    companion object {
        fun from(eventCounters: EventCountersResponse): EventCounters =
            with(eventCounters) {
                EventCounters(
                    booked ?: 0,
                    canceled ?: 0,
                    confirmation ?: 0,
                    finished ?: 0,
                    in_work ?: 0,
                    pending_payment ?: 0,
                    unread ?: 0,
                    need_attention ?: 0
                )
            }
    }

}