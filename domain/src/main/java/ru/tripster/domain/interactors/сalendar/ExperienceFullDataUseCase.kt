package ru.tripster.domain.interactors.сalendar

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.calendar.filtering.Experience

interface ExperienceFullDataUseCase {

    suspend operator fun invoke(isVisible: Boolean): ActionResult<Experience>

}