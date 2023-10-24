package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants
import com.name.domain.interactors.UpdateProfileUseCase
import com.name.domain.repository.ProfileRepo
import kotlinx.coroutines.withContext

class UpdateProfileUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : UpdateProfileUseCase {

    override suspend fun invoke(
        name: String?,
        avatarUri: String?,
        autoUnlockPaid: Boolean?,
        readingMode: Boolean?
    ): ActionResult<Any> =
        withContext(dispatcher.io) {
            when (val apiData = profileRepo.updateProfile(name, avatarUri, autoUnlockPaid,readingMode)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        ActionResult.Success(it)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}