package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.utils.Constants
import com.fiction.domain.interactors.UpdateProfileUseCase
import com.fiction.domain.repository.ProfileRepo
import kotlinx.coroutines.withContext

class UpdateProfileUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val profileInfoUseCase: GetProfileInfoUseCase,
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
                        readingMode?.let { profileInfoUseCase.updateReadingMode(it) }
                        autoUnlockPaid?.let { profileInfoUseCase.updateAutoUnlockMode(it) }
                        ActionResult.Success(it)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}