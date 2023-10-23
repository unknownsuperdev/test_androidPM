package ru.tripster.guide.enums

enum class OrderTypes (val type: String) {
    PRIVATE("Индивидуальная экскурсия"),
    GROUP("Групповая экскурсия"),
    TOUR("Авторский тур"),
    TICKET("Билет")
}