package ru.tripster.domain.interactors.сalendar

import kotlinx.coroutines.flow.Flow

interface GetExperienceIdUseCase {

    suspend operator fun invoke() : Flow<Int?>

}
