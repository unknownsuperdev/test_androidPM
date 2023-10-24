package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants
import com.name.domain.interactors.RemoveProfileAvatarUseCase
import com.name.domain.repository.ProfileRepo
import kotlinx.coroutines.withContext

class RemoveProfileAvatarUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : RemoveProfileAvatarUseCase {

    override suspend fun invoke(): ActionResult<Any> =
        withContext(dispatcher.io) {

            when (val apiData = profileRepo.removeProfileAvatar()) {
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