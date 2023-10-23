package ru.tripster.guide.ui.calendar.ui

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isGone
import ru.tripster.guide.R
import ru.tripster.guide.ui.calendar.model.CalendarDay

internal class WeekHolder(
    private val dayHolders: List<DayHolder>,
    private val isFromDateFilter: Boolean = false
) {

    private lateinit var container: LinearLayout

    fun inflateWeekView(parent: LinearLayout): View {
        container = LinearLayout(parent.context).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (isFromDateFilter)
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.space_52)
            else
                layoutParams.height = resources.getDimensionPixelSize(R.dimen.space_58)

            orientation = LinearLayout.HORIZONTAL
            weightSum = dayHolders.count().toFloat()
            for (holder in dayHolders) {
                addView(holder.inflateDayView(this))
            }
        }
        return container
    }

    fun bindWeekView(daysOfWeek: List<CalendarDay>) {
        container.isGone = daysOfWeek.isEmpty()
        dayHolders.forEachIndexed { index, holder ->
            // Indices can be null if OutDateStyle is NONE. We set the
            // visibility for the views at these indices to INVISIBLE.
            holder.bindDayView(daysOfWeek.getOrNull(index))
        }
    }

    fun reloadDay(day: CalendarDay): Boolean = dayHolders.any { it.reloadViewIfNecessary(day) }
}
