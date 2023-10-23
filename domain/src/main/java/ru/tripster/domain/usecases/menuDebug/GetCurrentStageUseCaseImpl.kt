package ru.tripster.domain.usecases.menuDebug

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.menuDebug.GetCurrentStageUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetCurrentStageUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GetCurrentStageUseCase {
    override suspend fun invoke(): Flow<String?> = withContext(dispatcher.io) {
        dataStoreRepository.getCurrentStage()
    }
}
