package ru.tripster.domain.usecases.confirm

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import ru.tripster.domain.interactors.confirmOrEdit.ChooseTourHourUseCase
import ru.tripster.domain.model.order.ChooseTime
import java.time.LocalTime
import java.util.*

class ChooseTourHourUseCaseImpl : ChooseTourHourUseCase {
    private val timeList = mutableListOf<ChooseTime>()
    private var isChosen = false

    override fun countTotalPrice(chosenHour: String, listStartTime: String): List<ChooseTime> {

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()

        val date: Date = sdf.parse(listStartTime)
        calendar.time = date

        while (timeList.size <= 47) {
            isChosen = chosenHour == sdf.format(calendar.time)

            timeList.add(ChooseTime(timeList.size, sdf.format(calendar.time), isChosen))
            calendar.add(Calendar.MINUTE, 30)
        }

        return timeList
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun getTimeListForCloseTime(
        chosenHour: String,
        listStartTime: String,
        startTime: String,
        endTime: String
    ): List<ChooseTime> {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()

        val date: Date = sdf.parse(listStartTime)
        calendar.time = date

        while (timeList.size <= 47) {
            isChosen =
                (if (chosenHour == "24:00") "00:00" else chosenHour) == sdf.format(calendar.time)
            timeList.add(
                ChooseTime(
                    timeList.size,
                    sdf.format(calendar.time),
                    isChosen,
                    when (chosenHour) {
                        startTime -> startTime.dayIsValid(
                            sdf.format(calendar.time).toString(),
                            endTime,
                            true,
                            listStartTime
                        )

                        endTime -> endTime.dayIsValid(
                            sdf.format(calendar.time).toString(),
                            startTime, false, listStartTime
                        )
                        else -> null
                    }
                )
            )
            calendar.add(Calendar.MINUTE, 30)
        }

        timeList.removeIf {
            it.isValid == false
        }

        return if (listStartTime == "00:30") {
            val item = timeList.find { chosenTime -> chosenTime.time == "00:00" }
            timeList.replaceAll {
                if (it.time == "00:00") ChooseTime(
                    item?.id ?: 0,
                    "24:00",
                    item?.isChecked ?: false,
                    item?.isValid
                ) else ChooseTime(it.id, it.time, it.isChecked, it.isValid)
            }
            timeList
        } else timeList
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun String.dayIsValid(
        hourFromList: String,
        secondHour: String,
        isFirstHour: Boolean,
        listStartTime: String
    ): Boolean {

        var startTimeParse: LocalTime? = null
        var hourFromListParse: LocalTime? = null
        var secondTimeParse: LocalTime? = null

        if (this.isNotEmpty()) startTimeParse = LocalTime.parse(if (this == "24:00") "00:00" else this)
        if (hourFromList.isNotEmpty()) hourFromListParse = LocalTime.parse(hourFromList)
        if (secondHour.isNotEmpty()) secondTimeParse = LocalTime.parse(if (secondHour == "24:00") "00:00" else secondHour)

        val isValid = when {

            secondHour.isEmpty() && this.isEmpty() -> true

            isFirstHour && (secondHour.isEmpty() || secondHour == "24:00") -> true

            !isFirstHour && secondHour.isEmpty() -> true

            listStartTime == "00:30" && hourFromList == "00:00" -> true

            !isFirstHour && !(secondTimeParse?.isAfter(hourFromListParse) == true || secondTimeParse == hourFromListParse) -> true

            isFirstHour && (secondTimeParse?.isAfter(hourFromListParse) == true) -> true

            else -> false
        }

        return isValid
    }
}

