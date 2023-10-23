package ru.tripster.domain.usecases.notifications

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.notifications.GetNotificationStateUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetNotificationStateUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GetNotificationStateUseCase {
    override suspend fun invoke(): Boolean = withContext(dispatcher.io) {
        dataStoreRepository.getNotificationState().first() ?: false
    }
}