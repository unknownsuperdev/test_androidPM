package ru.tripster.domain.usecases.order

import kotlinx.coroutines.flow.Flow
import ru.tripster.domain.interactors.order.GetGuideEmailUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetGuideEmailUseCaseImpl(private val dataStoreRepository: DataStoreRepository) :
    GetGuideEmailUseCase {
    override suspend fun invoke(): Flow<String?> = dataStoreRepository.getGuidEmail()

}