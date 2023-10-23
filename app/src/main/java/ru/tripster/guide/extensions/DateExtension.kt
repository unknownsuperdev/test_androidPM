package ru.tripster.guide.extensions

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import ru.tripster.guide.R
import java.time.LocalDate

fun LocalDate?.selectedDateFormat(
    context: Context,
    secondSelectedDate: LocalDate?,
    containerChosenDays: LinearLayout?,
    containerButtons: LinearLayout?,
    events: TextView?,
    showContainerChosenDays: Boolean
): String {
    val firstDateDay = this?.dayOfMonth.toString()
    val firstDateMonth = this?.monthValue
    val secondDateDay = secondSelectedDate?.dayOfMonth.toString()
    val secondDateMonth = secondSelectedDate?.monthValue
    when {
        secondSelectedDate != null -> {
            containerChosenDays?.isVisible = showContainerChosenDays
            containerButtons?.makeVisible()
            events?.text = context.getString(R.string.events_on_that_days)

            return if (firstDateMonth != secondDateMonth)
                "$firstDateDay ${
                    firstDateMonth.toString().dateOnlyMonth()
                } — $secondDateDay ${secondDateMonth.toString().dateOnlyMonth()}"
            else
                "$firstDateDay–$secondDateDay ${
                    secondDateMonth.toString().dateOnlyMonth()
                }"
        }
        this != null -> {
            containerButtons?.makeVisible()
            containerChosenDays?.isVisible = showContainerChosenDays
            events?.text = context.getString(R.string.events_on_that_day)
            return "$firstDateDay ${firstDateMonth.toString().dateOnlyMonth()}"
        }
        else -> {
            containerButtons?.makeVisibleGone()
            return ""
        }
    }
}