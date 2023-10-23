package ru.tripster.domain.model.events

enum class EventTypes(val type: String) {
    PRIVATE("private"),
    GROUP("group"),
    TOUR("tour"),
    TICKET("ticket")
}
