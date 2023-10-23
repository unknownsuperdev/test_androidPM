package ru.tripster.domain.model.calendar

import ru.tripster.entities.response.calendar.ScheduleResponse

enum class CalendarDayType {
    FREE_DAY,
    CLOSED_TIME,
    CLOSED_DAY,
    HAVE_RESERVATION,
    BOOKED_ORDER_TIME_CLOSED
}

fun List<ScheduleResponse>?.getType(): CalendarDayType {
    var isHaveEvent = false
    var isCloseTime = false
    var isClosedDay = false
    if (isNullOrEmpty()) {
        return CalendarDayType.FREE_DAY
    } else {
        forEach {
            if (it.type?.contains("busy") == true) isCloseTime = true
            if (it.type?.contains("event") == true) isHaveEvent = true
            if (it.duration == 1440 || it.duration == 1439) isClosedDay = true
        }
    }
    return when {
        isHaveEvent && isCloseTime -> CalendarDayType.BOOKED_ORDER_TIME_CLOSED
        isCloseTime && isClosedDay -> CalendarDayType.CLOSED_DAY
        isCloseTime -> CalendarDayType.CLOSED_TIME
        isHaveEvent -> CalendarDayType.HAVE_RESERVATION
        else -> CalendarDayType.FREE_DAY
    }
}