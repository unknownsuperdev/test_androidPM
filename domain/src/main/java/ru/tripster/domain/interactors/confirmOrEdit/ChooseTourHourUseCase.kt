package ru.tripster.domain.interactors.confirmOrEdit

import ru.tripster.domain.model.order.ChooseTime

interface ChooseTourHourUseCase {
    fun countTotalPrice(
        chosenHour: String,
        listStartTime: String
    ): List<ChooseTime>

    fun getTimeListForCloseTime(
        chosenHour: String,
        listStartTime: String,
        startTime: String, endTime: String
    ): List<ChooseTime>
}