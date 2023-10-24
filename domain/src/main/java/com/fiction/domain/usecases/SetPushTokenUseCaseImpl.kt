package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.SavePushTokenLocalUseCase
import com.fiction.domain.interactors.SetPushTokenUseCase
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.utils.Constants
import com.fiction.entities.response.registration.PushToken
import kotlinx.coroutines.withContext

class SetPushTokenUseCaseImpl(
    private val authRepository: AuthRepository,
    private val savePushTokenLocalUseCase: SavePushTokenLocalUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : SetPushTokenUseCase {

    override suspend fun invoke(pushToken: String): ActionResult<String> =
        withContext(dispatcher.io) {
            when (val apiData = authRepository.setPushToken(PushToken(pushToken))) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        savePushTokenLocalUseCase(pushToken)
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}