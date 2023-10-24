package com.name.domain.usecases

import com.name.core.ActionResult
import com.name.core.CallException
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.Constants
import com.name.domain.interactors.GetProfileInfoUseCase
import com.name.domain.model.profile.ProfileInfo
import com.name.domain.repository.ProfileRepo
import kotlinx.coroutines.withContext

class GetProfileInfoUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val dispatcher: CoroutineDispatcherProvider
) : GetProfileInfoUseCase {
    override suspend fun invoke(): ActionResult<ProfileInfo> =
        withContext(dispatcher.io) {

            when (val apiData = profileRepo.getProfileInfo()) {
                is ActionResult.Success -> {

                    apiData.result.data?.let {
                        val profileInfo = ProfileInfo.from(it)
                        ActionResult.Success(profileInfo)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}