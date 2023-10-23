package ru.tripster.domain.usecases.notifications

import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.notifications.SetNotificationStateUseCase
import ru.tripster.domain.repository.DataStoreRepository

class SetNotificationStateUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SetNotificationStateUseCase {
    override suspend fun invoke(isSelected: Boolean) = withContext(dispatcher.io) {
        dataStoreRepository.saveNotificationState(isSelected)
    }
}