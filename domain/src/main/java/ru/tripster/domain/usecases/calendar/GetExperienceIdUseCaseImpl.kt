package ru.tripster.domain.usecases.calendar

import kotlinx.coroutines.flow.Flow
import ru.tripster.domain.interactors.сalendar.GetExperienceIdUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetExperienceIdUseCaseImpl (private val dataStoreRepository: DataStoreRepository) : GetExperienceIdUseCase {
    override suspend fun invoke(): Flow<Int?> = dataStoreRepository.getExperienceIdFiltration()

}
