package ru.tripster.domain.interactors.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.calendar.GuideDateSchedule

interface GetGuideDateScheduleUseCase {
    suspend operator fun invoke(
        date: String,
        experienceId: Int?,
        experienceTitle: String?
    ): ActionResult<GuideDateSchedule>
}