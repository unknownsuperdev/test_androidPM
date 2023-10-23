package ru.tripster.domain.model.chat

enum class UserStatusChat(val value: String) {
    CONFIRM("confirm"),
    UPDATE("update"),
    BOOKED("booked"),
    REJECT("reject"),
    CREATED("created")
}
