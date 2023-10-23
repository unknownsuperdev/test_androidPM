package ru.tripster.guide.enums

enum class RejectedOrderTypes(val type: String) {

    CAN_NOT_TAKE("Не могу провести"),
    TRAVELLER_REFUSED("Путешественник отказался"),
    TRAVELLER_NOT_RESPOND("Путешественник не отвечает"),
    BUSY_DAY("Заказ на занятый день"),
    DUPLICATED_ORDER("Дубликат другого заказа"),
    NOT_ABOUT_EXPERIENCE("Заказ не соответствует условиям")
}