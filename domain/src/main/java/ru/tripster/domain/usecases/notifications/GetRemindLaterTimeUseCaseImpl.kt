package ru.tripster.domain.usecases.notifications

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.notifications.GetRemindLaterTimeUseCase
import ru.tripster.domain.repository.DataStoreRepository

class GetRemindLaterTimeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : GetRemindLaterTimeUseCase {
    override suspend fun invoke(): String = withContext(dispatcher.io) {
        dataStoreRepository.getLastRemindLaterTime().first().toString()
    }
}