package ru.tripster.domain.interactors.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.calendar.CloseTimeSchedule

interface CloseTimeUseCase {
    suspend operator fun invoke(
        closeTime: CloseTimeSchedule,
        experienceId: Int,
        experienceTitle: String
    ): ActionResult<Unit>
}