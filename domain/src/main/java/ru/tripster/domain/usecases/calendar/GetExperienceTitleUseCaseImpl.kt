package ru.tripster.domain.usecases.calendar

import kotlinx.coroutines.flow.Flow
import ru.tripster.domain.interactors.сalendar.GetExperienceTitleUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetExperienceTitleUseCaseImpl(private val dataStoreRepository: DataStoreRepository) :
    GetExperienceTitleUseCase {
    override suspend fun invoke(): Flow<String?> =
        dataStoreRepository.getExperienceTitleFiltration()

}
