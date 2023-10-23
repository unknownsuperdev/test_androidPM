package ru.tripster.domain.usecases.profile

import android.content.ContentValues.TAG
import android.util.Log
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.profile.LogOutUseCase
import ru.tripster.domain.repository.DataStoreRepository

class LogOutUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val dataStoreRepository: DataStoreRepository,
) : LogOutUseCase {

    override suspend fun invoke(): String = withContext(dispatcher.io) {
        dataStoreRepository.saveToken("")
        dataStoreRepository.getUserData().first().toString()
    }

}