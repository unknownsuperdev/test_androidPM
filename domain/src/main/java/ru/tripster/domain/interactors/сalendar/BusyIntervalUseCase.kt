package ru.tripster.domain.interactors.сalendar

import ru.tripster.core.ActionResult

interface BusyIntervalUseCase {
    suspend operator fun invoke(
        id: Int
    ): ActionResult<Boolean>
}