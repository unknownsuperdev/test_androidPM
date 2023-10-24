package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.UpdateDeviceUseCase
import com.fiction.domain.model.UpdateDevice
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext
import java.util.*

class UpdateDeviceUseCaseImpl(
    private val authRepository: AuthRepository,
    private val dispatcher: CoroutineDispatcherProvider,
) : UpdateDeviceUseCase {

    override suspend fun invoke(updateDevice: UpdateDevice): ActionResult<String> =
        withContext(dispatcher.io) {
            val timeZone: TimeZone = TimeZone.getDefault()
            val updateDeviceRequest = UpdateDevice.to(updateDevice, timeZone.id)
            when (val apiData =
                authRepository.updateDevice(updateDeviceRequest)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
