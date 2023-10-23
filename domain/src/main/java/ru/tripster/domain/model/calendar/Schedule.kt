package ru.tripster.domain.model.calendar

import ru.tripster.entities.response.calendar.ScheduleResponse

data class Schedule(
    val type: String,
    val id: Int,
    val time: String,
    val experienceId: Int,
    val duration: Int
) {
    companion object {
        fun from(scheduleResponse: ScheduleResponse): Schedule =
            with(scheduleResponse) {
                Schedule(
                    type = type ?: "",
                    id = id ?: 0,
                    time = time ?: "",
                    experienceId = experience_id ?: 0,
                    duration = duration ?: 0
                )
            }
    }
}