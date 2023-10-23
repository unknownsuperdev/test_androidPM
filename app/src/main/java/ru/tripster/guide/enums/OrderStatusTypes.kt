package ru.tripster.guide.enums

enum class OrderStatusTypes(val type: String) {
    MESSAGE("Переписка"),
    CONFIRMATION("Ждет подтверждения"),
    PENDING_PAYMENT("Ожидает оплаты"),
    BOOKED("Забронирован"),
    FINISHED("Завершен"),
    CANCELLED_BY_GUIDE("Отменен гидом"),
    CANCELLED_BY_TRAVELLER("Отменен путешественником"),
    BOOKING_OPEN("Открыто бронирование"),
    BOOKING_CLOSED("Закрыто бронирование"),
    CANCELLED ("Отменен")
}