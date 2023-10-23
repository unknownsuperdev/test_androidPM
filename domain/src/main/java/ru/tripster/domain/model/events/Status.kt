package ru.tripster.domain.model.events

enum class Status(val value: String) {
    CONFIRM("confirmation"),
    PENDING_PAYMENT("pending_payment"),
    PAID("paid"),
    CANCELLED("cancelled"),
    MESSAGING ("messaging")
}