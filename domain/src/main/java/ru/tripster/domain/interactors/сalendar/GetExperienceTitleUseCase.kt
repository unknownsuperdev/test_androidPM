package ru.tripster.domain.interactors.сalendar

import kotlinx.coroutines.flow.Flow

interface GetExperienceTitleUseCase {
    suspend operator fun invoke() : Flow<String?>
}
