package ru.tripster.guide.ui.calendar.ui

import android.view.View
import ru.tripster.guide.ui.calendar.model.CalendarDay
import ru.tripster.guide.ui.calendar.model.CalendarMonth

open class ViewContainer(val view: View)

interface DayBinder<T : ViewContainer> {
    fun create(view: View): T
    fun bind(container: T, day: CalendarDay)
}

interface MonthHeaderFooterBinder<T : ViewContainer> {
    fun create(view: View): T
    fun bind(container: T, month: CalendarMonth)
}

typealias MonthScrollListener = (CalendarMonth) -> Unit
