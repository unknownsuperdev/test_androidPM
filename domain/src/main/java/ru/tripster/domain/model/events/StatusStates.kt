package ru.tripster.domain.model.events

enum class StatusStates(val number:Int) {
    NONE(0),
    CONFIRM(1),
    PENDING_PAYMENT(2),
    BOOKED(3),
    CANCELLED(4)
}