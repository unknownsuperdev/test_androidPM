package ru.tripster.domain.repository.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.entities.request.calendar.closeTime.CloseTimeRequest

interface CloseTimeRepository {
    suspend fun addCloseTime(
        closeTime: CloseTimeRequest
    ): ActionResult<Unit>
}