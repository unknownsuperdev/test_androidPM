package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.model.profile.ProfileInfo
import com.fiction.domain.repository.DataStoreRepository
import com.fiction.domain.repository.ProfileRepo
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class GetProfileInfoUseCaseImpl(
    private val profileRepo: ProfileRepo,
    private val dispatcher: CoroutineDispatcherProvider,
    private val dataStoreRepository: DataStoreRepository
) : GetProfileInfoUseCase {
    private var profileInfo: ProfileInfo? = null

    override suspend fun invoke(isMakeCallAnyWay: Boolean, isClear: Boolean): ActionResult<ProfileInfo> =
        withContext(dispatcher.io) {
            if (isClear) {
                profileInfo = null
                return@withContext ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
            }
            if (profileInfo != null && !isMakeCallAnyWay) ActionResult.Success(profileInfo!!)
            else
                when (val apiData = profileRepo.getProfileInfo()) {
                    is ActionResult.Success -> {
                        apiData.result.data?.let {
                            val profile = ProfileInfo.from(it)
                            profileInfo = profile
                            dataStoreRepository.setUserId(profile.uuid)
                            ActionResult.Success(profile)
                        } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                    }

                    is ActionResult.Error -> {
                        ActionResult.Error(apiData.errors)
                    }
                }
        }

    override suspend fun updateBalance(balance: Int) {
        profileInfo?.let {
            profileInfo = it.copy(balance = balance)
        }
    }

    override fun updateReadingMode(readingMode: Boolean) {
        profileInfo?.let {
            val setting = it.setting.copy(readingMode = readingMode)
            profileInfo = it.copy(setting = setting)
        }
    }

    override fun updateAutoUnlockMode(autoUnlock: Boolean) {
        profileInfo?.let {
            val setting = it.setting.copy(autoUnlockPaid = autoUnlock)
            profileInfo = it.copy(setting = setting)
        }
    }

}