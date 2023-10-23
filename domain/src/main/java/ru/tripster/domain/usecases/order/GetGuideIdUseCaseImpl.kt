package ru.tripster.domain.usecases.order

import kotlinx.coroutines.flow.Flow
import ru.tripster.domain.interactors.order.GetGuideIdUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetGuideIdUseCaseImpl(private val dataStoreRepository: DataStoreRepository) :
    GetGuideIdUseCase {
    override suspend fun invoke(): Flow<Int?> = dataStoreRepository.getGuidId()
}