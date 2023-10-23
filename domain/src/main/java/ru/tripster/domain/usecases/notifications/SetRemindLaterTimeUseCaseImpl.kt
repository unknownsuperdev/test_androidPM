package ru.tripster.domain.usecases.notifications

import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.notifications.SetRemindLaterTimeUseCase
import ru.tripster.domain.repository.DataStoreRepository

class SetRemindLaterTimeUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SetRemindLaterTimeUseCase {
    override suspend fun invoke(time: String)  = withContext(dispatcher.io) {
        dataStoreRepository.saveLastRemindLaterTime(time)
    }
}