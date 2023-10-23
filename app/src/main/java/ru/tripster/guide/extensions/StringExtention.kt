package ru.tripster.guide.extensions

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import ru.tripster.domain.model.chat.UserRoleChat
import ru.tripster.domain.model.events.EventTypes
import ru.tripster.domain.model.events.Status
import ru.tripster.guide.R
import ru.tripster.guide.enums.OrderStatusTypes
import ru.tripster.guide.enums.OrderTypes
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.math.abs

private fun String.monthChange(): String {
    return when {
        contains("июн") -> replace("июн", "июня")
        contains("июл") -> replace("июл", "июля")
        contains("нояб") -> replace("нояб", "ноя")
        contains("сент") -> replace("сент", "сен")
        contains("авгу") -> replace("авгу", "авг")
        contains("февр") -> replace("февр", "фев")
        else -> this
    }
}

fun String.dateFormattingWithHour(duration: Double?): String {
    return if (this.length < 16) {
        this
    } else {
        val formattedDate = this.substring(0..15)
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale("ru"))
        val sdfTime = SimpleDateFormat("HH:mm", Locale("ru"))
        val date = sdf.parse(formattedDate)

        sdf.applyPattern("d MMM, EEE HH:mm")
        calendar.time = date
        duration?.let { calendar.add(Calendar.MINUTE, (duration * 60).toInt()) }
        "${sdf.format(date)}–${sdfTime.format(calendar.time)}".replace(".", "").monthChange()
    }
}

fun String.fromDurationToHour(duration: Int): String {
    return if (this.length < 16) {
        this
    } else {
        val formattedDate = this.substring(0..15)
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale("ru"))
        val sdfTime = SimpleDateFormat("HH:mm", Locale("ru"))
        val date = sdf.parse(formattedDate)

        sdf.applyPattern("d MMM, EEE HH:mm")
        calendar.time = date
        calendar.add(Calendar.MINUTE, duration)
        "${sdf.format(date)}–${sdfTime.format(calendar.time)}".replace(".", "").monthChange()
    }

}

fun String.toDate(): Date {
    return SimpleDateFormat("HH:mm", Locale.getDefault()).parse(this)
}

fun String.countDuration(endTime: String): Int {
    val calendarStartTime = Calendar.getInstance()
    val calendarEndTime = Calendar.getInstance()
    val format = SimpleDateFormat("HH:mm", Locale.getDefault())
    calendarStartTime.time = format.parse(this)
    calendarEndTime.time = format.parse(endTime)
    val durationInMillis = abs(calendarEndTime.timeInMillis - calendarStartTime.timeInMillis)
    val durationInMinutes = durationInMillis / (1000 * 60)
    return durationInMinutes.toInt()
}

fun String.check24Hours(): Boolean {
    return if (this.isEmpty()) {
        false
    } else {
        val formattedDate = this.substring(0..15)
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
        val date = sdf.parse(formattedDate)

        calendar.time = date
        calendar.add(Calendar.HOUR, 24)

        Calendar.getInstance().time > calendar.time
    }
}

fun String.dateFormattingOnlyDate(): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    val date = sdf.parse(this)

    sdf.applyPattern("d MMM, EEE")
    calendar.time = date

    return sdf.format(date).replace(".", "").monthChange()
}

fun String.dateFormattingOnlyHour(): String {
    return  if (this.length < 16) {
        this
    } else {
        val formattedDate = this.substring(0..15)
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale("ru"))
        val date = sdf.parse(this)

        sdf.applyPattern("HH:mm")
        calendar.time = date

        sdf.format(date)
    }
}

fun String.isVisitedOrder(lastModifiedDate: String): Boolean {
    val lastVisitFormattedDate: String
    val lastModifiedFormattedDate: String
    var dateOfGuideVisit: Date? = null
    var dateOfLastModified: Date? = null
    if (this != "" && lastModifiedDate != "") {
        lastVisitFormattedDate = this.substring(0..18)
        lastModifiedFormattedDate = lastModifiedDate.substring(0..18)
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru"))
        dateOfGuideVisit = sdf.parse(lastVisitFormattedDate)
        dateOfLastModified = sdf.parse(lastModifiedFormattedDate)

    }
    val cmp = dateOfGuideVisit?.compareTo(dateOfLastModified)
    if (cmp != null) {
        return cmp >= 0
    }
    return false
}

fun String.hourAndDuration(date: String?, duration: Double?): String {
    val format = "${date}T${this}"
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault())
    val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateSdf = sdf.parse(format)
    calendar.time = dateSdf

    duration?.let { calendar.add(Calendar.MINUTE, (duration * 60).toInt()) }

    return "$this–${sdfTime.format(calendar.time)}"
}

fun String.hourAndDuration(duration: Double? = null): String {
    return if (this.isNotEmpty()) {
        val format = this
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val sdfTime = SimpleDateFormat("HH:mm", Locale.getDefault())
        val dateSdf = sdf.parse(format)
        calendar.time = dateSdf
        duration?.let { calendar.add(Calendar.MINUTE, (duration * 60).toInt()) }
        "$this-${sdfTime.format(calendar.time)}"
    } else ""

}

fun String.calcMonth(monthCount: Int?): String {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    monthCount?.let {
        calendar.add(Calendar.MONTH, monthCount)
    }
    return sdf.format(calendar.time).toString()
}

fun String.isLastDayOfMonth(): Boolean {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val date = dateFormat.parse(this)

    val calendar = Calendar.getInstance()
    calendar.time = date

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    return day == lastDayOfMonth
}

@RequiresApi(Build.VERSION_CODES.O)

fun String.daysCount(secondDate: String): Long {

    return if (this.isEmpty() || secondDate.isEmpty()) return 0 else {
        val startDate = LocalDate.of(
            this.take(4).toInt(),
            this.substring(5..6).toInt(),
            this.takeLast(2).toInt()
        )
        val endDate = LocalDate.of(
            secondDate.take(4).toInt(),
            secondDate.substring(5..6).toInt(),
            secondDate.takeLast(2).toInt()
        )

        return ChronoUnit.DAYS.between(startDate, endDate)
    }

}

fun String.addDays(daysCount: Long): String {

    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//    if (this.length > 7) this.dropLast(3)
    val date = dateFormat.parse(this)
    calendar.time = date

//    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    calendar.add(Calendar.DATE, daysCount.toInt())

    return dateFormat.format(calendar.time).toString()
}

fun String.calcMonthForFullMonth(monthCount: Int?): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
    if (this.length > 7) this.dropLast(3)
    val date = dateFormat.parse(this)

    calendar.time = date

    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    monthCount?.let {
        calendar.add(Calendar.MONTH, it)
        if (it < 0)
            calendar.set(Calendar.DAY_OF_MONTH, 1) else
            calendar.set(
                Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
    }

    return sdf.format(calendar.time).toString()
}

fun String.dateFormatting(): String {
    return if (this.isNotEmpty()) {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
        val date = sdf.parse(this)

        sdf.applyPattern("d MMM, EEE")
        calendar.time = date

        sdf.format(date).replace(".", "").monthChange()
    } else ""

}

fun String.parseDate(): Date {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))

    return sdf.parse(this)
}

fun String.phoneCall(context: Context) {
    val callIntent = Intent(Intent.ACTION_DIAL)
    this.let {
        callIntent.data = Uri.parse("tel:${it}")
        ContextCompat.startActivity(context, callIntent, null)
    }
}

fun String.currency(): String {
    return when (this) {
        "RUB", " ₽" -> " ₽"
        "EUR", "€" -> "€"
        else -> "$"
    }
}

private fun priceSpacing(price: Int?) =
    price.toString().run {
        when {
            length > 6 -> {
                var empty = ""
                for (i in lastIndex downTo 0) {
                    if (i != 6 && i % 3 == 0) {
                        empty += " ${this[i]}"
                    } else {
                        empty += this[i]
                    }
                }
                empty.reversed()
            }
            length > 4 -> {
                dropLast(3) + " ${substring(lastIndex - 2)}"
            }
            else -> this@run
        }
    }

fun String.setParticipantAndCurrency(
    participant: String?, value: Int?, currency: String?
): String {
    val count = priceSpacing(value)
    return if (participant.isNullOrEmpty()) {
        when (currency) {
            " ₽" -> this.format(count, currency)
            else -> this.format(currency, count)
        }
    } else {
        when (currency) {
            " ₽" -> this.format(participant, count, currency)
            else -> this.format(participant, currency, count)
        }
    }
}

fun String.setTripsterCurrency(
    value: Int?, currency: String?
): String {
    val count = priceSpacing(value)
    return when (currency) {
        " ₽" -> this.format(count, currency)
        else -> this.format(currency, count)
    }
}


@SuppressLint("UseCompatLoadingForDrawables")
fun String.statusType(
    textView: AppCompatTextView,
    context: Context?,
    awareStartDt: String,
    initiator: String,
    isVisited: Boolean,
    typeOfView: String,
    progress: Float,
    booleanOfBooked: ((Boolean) -> Unit)? = null
) {
    when (this) {
        Status.CONFIRM.value, Status.MESSAGING.value -> {
            textView.background =
                context?.getDrawable(R.drawable.shape_tomat_rectangle_4)
            context?.getColor(R.color.white)?.let { textView.setTextColor(it) }
            textView.text = context?.getString(R.string.waiting_for_confirmation_count)
        }
        Status.PAID.value -> {
            booleanOfBooked?.invoke(true)

            if (awareStartDt.check24Hours()) {
                when {
                    typeOfView == context?.getString(R.string.private_type) && progress in 0.0f..0.5f -> {
                        textView.setTypeOfStatus(
                            R.drawable.shape_white_30_rectangle_4,
                            R.color.white,
                            context.getString(R.string.ended)
                        )
                    }
                    else -> {
                        textView.setTypeOfStatus(
                            R.drawable.shape_white_gray_90_rectangle_4,
                            R.color.gray_20,
                            context?.getString(R.string.ended) ?: ""
                        )
                    }
                }
            } else {
                textView.background =
                    context?.getDrawable(R.drawable.shape_green_rectangle_4)
                context?.getColor(R.color.white)?.let { textView.setTextColor(it) }
                textView.text = context?.getString(R.string.booked_order)
            }
        }
        Status.PENDING_PAYMENT.value -> {
            if (typeOfView == context?.getString(R.string.private_type) && progress != 1.0f) {
                textView.setTypeOfStatus(
                    R.drawable.shape_white_30_rectangle_4,
                    R.color.white,
                    context.getString(R.string.pending_payment_status)
                )
            } else {
                textView.background =
                    context?.getDrawable(R.drawable.shape_white_gray_90_rectangle_4)
                context?.getColor(R.color.gray_20)?.let { textView.setTextColor(it) }
                textView.text = context?.getString(R.string.pending_payment_status)
            }
        }
        Status.CANCELLED.value -> {
            when (initiator) {
                context?.getString(R.string.traveler_type) -> {
                    if (isVisited) {
                        if (typeOfView == context.getString(R.string.private_type) && progress != 1.0f) {
                            textView.setTypeOfStatus(
                                R.drawable.shape_white_30_rectangle_4,
                                R.color.white,
                                context.getString(R.string.canceled_by_traveler)
                            )
                        } else {
                            textView.setTypeOfStatus(
                                R.drawable.shape_white_gray_90_rectangle_4,
                                R.color.gray_20,
                                context.getString(R.string.canceled_by_traveler)
                            )
                        }

                    } else {
                        textView.setTypeOfStatus(
                            R.drawable.shape_orange_rectangle_4,
                            R.color.white,
                            context.getString(R.string.canceled_by_traveler)
                        )
                    }
                }
                context?.getString(R.string.guide_type) -> {
                    if (typeOfView == context.getString(R.string.private_type) && progress != 1.0f) {
                        textView.setTypeOfStatus(
                            R.drawable.shape_white_30_rectangle_4,
                            R.color.white,
                            context.getString(R.string.canceled_by_guide)
                        )
                    } else {
                        textView.setTypeOfStatus(
                            R.drawable.shape_white_gray_90_rectangle_4,
                            R.color.gray_20,
                            context.getString(R.string.canceled_by_guide)
                        )
                    }
                }
                else -> {
                    if (typeOfView == context?.getString(R.string.private_type) && progress != 1.0f) {
                        textView.setTypeOfStatus(
                            R.drawable.shape_white_30_rectangle_4,
                            R.color.white,
                            context.getString(R.string.canceled_by_system)
                        )
                    } else {
                        textView.setTypeOfStatus(
                            R.drawable.shape_white_gray_90_rectangle_4,
                            R.color.gray_20,
                            context?.getString(R.string.canceled_by_system) ?: ""
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun String.statusTypeGroupTour(
    textView: AppCompatTextView,
    context: Context?,
    awareStartDt: String,
    progress: Float
) {
    when (this) {
        Status.CANCELLED.value -> {
            if (progress != 1.0f) {
                textView.background =
                    context?.getDrawable(R.drawable.shape_white_30_rectangle_4)
                context?.getColor(R.color.white)?.let { textView.setTextColor(it) }
                textView.text = context?.getString(R.string.canceled_event)
            } else {
                textView.background =
                    context?.getDrawable(R.drawable.shape_white_gray_90_rectangle_4)
                context?.getColor(R.color.gray_20)?.let { textView.setTextColor(it) }
                textView.text = context?.getString(R.string.canceled_event)
            }

        }
        Status.PAID.value -> {
            if (awareStartDt.check24Hours()) {
                if (progress != 1.0f) {
                    textView.background =
                        context?.getDrawable(R.drawable.shape_white_30_rectangle_4)
                    context?.getColor(R.color.white)?.let { textView.setTextColor(it) }
                    textView.text = context?.getString(R.string.ended)
                } else {
                    textView.background =
                        context?.getDrawable(R.drawable.shape_white_gray_90_rectangle_4)
                    context?.getColor(R.color.gray_20)?.let { textView.setTextColor(it) }
                    textView.text = context?.getString(R.string.ended)
                }
            } else {
                textView.background =
                    context?.getDrawable(R.drawable.shape_green_rectangle_4)
                context?.getColor(R.color.white)?.let { textView.setTextColor(it) }
                textView.text = context?.getString(R.string.booking_is_closed)
            }
        }
        Status.PENDING_PAYMENT.value -> {
            if (progress != 1.0f) {
                textView.background =
                    context?.getDrawable(R.drawable.shape_white_30_rectangle_4)
                context?.getColor(R.color.white)?.let { textView.setTextColor(it) }
                textView.text = context?.getString(R.string.booking_is_open)
            } else {
                textView.background =
                    context?.getDrawable(R.drawable.shape_white_gray_90_rectangle_4)
                context?.getColor(R.color.gray_20)?.let { textView.setTextColor(it) }
                textView.text = context?.getString(R.string.booking_is_open)
            }

        }
    }
}

fun String.openLinkInBrowser(): Intent {
    val uri: Uri = Uri.parse(this)
    return Intent(Intent.ACTION_VIEW, uri)

}

fun String.dateChange(): String {
    val year = this.dropLast(6)
    var month = this.substring(5, 7)
    var day = this.drop(8)
    when (day[0]) {
        '0' -> day = day.drop(1)
    }
    when (month) {
        "01" -> month = "янв"
        "02" -> month = "фев"
        "03" -> month = "мар"
        "04" -> month = "апр"
        "05" -> month = "май"
        "06" -> month = "июня"
        "07" -> month = "июля"
        "08" -> month = "авг"
        "09" -> month = "сен"
        "10" -> month = "окт"
        "11" -> month = "ноя"
        "12" -> month = "дек"
    }
    return "$day $month $year"

}

fun String.shortedSurname(): String {
    val countOfWord = this.replace("  ", " ").split(' ')
    return if (countOfWord.size != 1) "${countOfWord[0]} ${
        countOfWord[1].substring(
            0,
            1
        )
    }." else this

}

fun String.dateWithYear(): String {
    val date = this.dateFormattingOnlyDate().substringBefore(',')
    val weekDay = this.dateFormattingOnlyDate().substringAfter(',')
    return "$date ${this.substring(0, 4)},$weekDay"
}

fun String.dateToFullName(): String {
    val sdf = SimpleDateFormat("dd.MM.yyyy", Locale("ru"))
    val date = sdf.parse(this)
    sdf.applyPattern("d MMMM YYYY")

    return sdf.format(date)
}

fun String.dateOnlyMonth(): String {
    val sdf = SimpleDateFormat("MM", Locale("ru"))
    val date = sdf.parse(this)
    sdf.applyPattern("MMMM")

    return sdf.format(date)
}

fun String.dateAndYear(): String {
    val sdf = SimpleDateFormat("yyyy-MM-dd", Locale("ru"))
    val date = sdf.parse(this)
    sdf.applyPattern("d MMMM")

    return sdf.format(date)
}

fun String.openMail(): Intent {
    return Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$this"))
}

fun String.isTodayOrYesterday(context: Context, viewedByGuide: Boolean): String {
    var day = ""

    if (this.isNotEmpty()) {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Calendar.getInstance().time)

        val date = sdf.format(sdf.parse(this))
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(currentDate)

        when {
            !viewedByGuide -> {
                day = context.getString(R.string.unread_comments)
            }
            date.toString().takeLast(2) == currentDate.toString().takeLast(2) && date.toString()
                .take(4) == currentDate.toString().take(4) -> {
                day = context.getString(R.string.today)
            }

            sdf.format(calendar.apply { this.add(Calendar.DAY_OF_MONTH, -1) }.time) == date -> {
                day = context.getString(R.string.yesterday)
            }

            else -> {
                day = date.dateAndYear()
            }
        }
    }
    return day
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.convertStringToLocalDate(): LocalDate? {
    return if (this.isNotEmpty()) LocalDate.parse(
        this,
        DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
    ) else null
}

fun getCurrentDateAndTime(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
    return dateFormat.format(Calendar.getInstance().time) + "+03:00"
}

fun getCurrentDateInIso8601Format(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
    return dateFormat.format(Calendar.getInstance().time)
}

fun currentDate(): String =
    SimpleDateFormat("yyyy-MM-dd", Locale("ru")).format(Calendar.getInstance().time)

fun currentDateAndTime(): String =
    SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale("ru")).format(Calendar.getInstance().time)

fun String.statusValueIndividual(
    awareStartDt: String, initiator: String,
): String {
    return when {
        this == Status.MESSAGING.value -> OrderStatusTypes.MESSAGE.type
        this == Status.CONFIRM.value -> OrderStatusTypes.CONFIRMATION.type
        this == Status.PENDING_PAYMENT.value -> OrderStatusTypes.PENDING_PAYMENT.type
        this == Status.CANCELLED.value && initiator == UserRoleChat.TRAVELER.value -> OrderStatusTypes.CANCELLED_BY_TRAVELLER.type
        this == Status.CANCELLED.value && initiator == UserRoleChat.GUIDE.value -> OrderStatusTypes.CANCELLED_BY_GUIDE.type
        this == Status.PAID.value && !awareStartDt.check24Hours() -> OrderStatusTypes.BOOKED.type
        this == Status.PAID.value && awareStartDt.check24Hours() -> OrderStatusTypes.FINISHED.type
        else -> ""
    }
}

fun String.statusValueGroupTour(
    awareStartDt: String
): String {

    return when (this) {
        Status.CANCELLED.value -> OrderStatusTypes.CANCELLED.type
        Status.PAID.value -> {
            if (awareStartDt.check24Hours())
                OrderStatusTypes.FINISHED.type
            else
                OrderStatusTypes.BOOKING_CLOSED.type
        }
        Status.PENDING_PAYMENT.value ->
            OrderStatusTypes.BOOKING_OPEN.type
        else -> ""
    }
}

fun String.tourType(): String {
    return when (this) {
        EventTypes.PRIVATE.type -> OrderTypes.PRIVATE.type
        EventTypes.GROUP.type -> OrderTypes.GROUP.type
        EventTypes.TOUR.type -> OrderTypes.TOUR.type
        else -> OrderTypes.TICKET.type
    }
}