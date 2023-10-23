package ru.tripster.guide.ui.fragments.chat

enum class ChatItemTypeEnum(val number: Int) {
    SENT(0),
    RECEIVED(1),
    TICKETS(2),
    USER(3),
    ERROR(4);

    companion object {
        fun toEnum(a: Int) = when (a) {
            0 -> SENT
            1 -> RECEIVED
            2 -> TICKETS
            3 -> USER
            else -> ERROR
        }
    }
}