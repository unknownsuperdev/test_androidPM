package ru.tripster.domain.interactors.сalendar

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.tripster.domain.model.calendar.filtering.ExperienceResults

interface ExperienceUseCase {
    suspend operator fun invoke(isVisible: Boolean,isClosingTime:Boolean): Flow<PagingData<ExperienceResults>>
}