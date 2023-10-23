package ru.tripster.domain.usecases.calendar

import ru.tripster.domain.interactors.сalendar.SetExperienceIdUseCase
import ru.tripster.domain.repository.DataStoreRepository

class ExperienceIdUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository
) : SetExperienceIdUseCase {
    override suspend fun invoke(experienceId: Int) {
        dataStoreRepository.saveClosingTimeFiltrationId(experienceId)
    }
}