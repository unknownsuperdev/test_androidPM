package ru.tripster.domain.repository.сalendar

import ru.tripster.core.ActionResult

interface BusyIntervalRepository {
    suspend fun deleteBusyInterval(
        id: Int
    ): ActionResult<Unit>
}