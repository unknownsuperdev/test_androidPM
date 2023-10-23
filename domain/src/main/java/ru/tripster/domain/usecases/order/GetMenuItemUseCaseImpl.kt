package ru.tripster.domain.usecases.order

import kotlinx.coroutines.flow.Flow
import ru.tripster.domain.interactors.order.GetMenuItemUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetMenuItemUseCaseImpl(private val dataStoreRepository: DataStoreRepository) :
    GetMenuItemUseCase {
    override suspend fun invoke(): Flow<String?>  = dataStoreRepository.getMenuItem()
}