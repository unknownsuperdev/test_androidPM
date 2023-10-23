package ru.tripster.domain.interactors.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.calendar.GuidesSchedule

interface GetGuidesScheduleUseCase {
    suspend operator fun invoke(
        begin: String,
        end: String,
        experienceId: Int?,
        experienceTitle: String?,
        showEventData: Boolean?
    ): ActionResult<List<GuidesSchedule>>
}