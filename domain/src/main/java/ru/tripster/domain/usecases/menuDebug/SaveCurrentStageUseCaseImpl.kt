package ru.tripster.domain.usecases.menuDebug

import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.menuDebug.SaveCurrentStageUseCase
import ru.tripster.domain.repository.DataStoreRepository

class SaveCurrentStageUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SaveCurrentStageUseCase {
    override suspend fun invoke(stage: String) = withContext(dispatcher.io) {
        dataStoreRepository.saveCurrentStage(stage)
    }
}
