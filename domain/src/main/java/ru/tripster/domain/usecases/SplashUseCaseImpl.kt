package ru.tripster.domain.usecases

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.SplashUseCase
import ru.tripster.domain.repository.DataStoreRepository

class SplashUseCaseImpl(
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : SplashUseCase {
    override suspend fun invoke(deviceId: String): String = withContext(dispatcher.io) {
        dataStoreRepository.saveDeviceId(deviceId)
        dataStoreRepository.getToken().first().toString()
    }
}