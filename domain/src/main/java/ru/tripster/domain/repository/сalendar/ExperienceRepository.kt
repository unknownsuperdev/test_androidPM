package ru.tripster.domain.repository.сalendar

import androidx.paging.Pager
import ru.tripster.core.ActionResult
import ru.tripster.entities.response.calendar.filtering.ExperienceResponse
import ru.tripster.entities.response.calendar.filtering.ExperienceTitleResponseModel

interface ExperienceRepository {
    suspend fun getExperience(isVisible: Boolean): Pager<Int, ExperienceTitleResponseModel>
    suspend fun getExperienceFullData(isVisible: Boolean): ActionResult<ExperienceResponse>
}