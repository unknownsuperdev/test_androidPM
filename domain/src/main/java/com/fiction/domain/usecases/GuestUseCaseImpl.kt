package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GuestUseCase
import com.fiction.domain.model.registration.Token
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.repository.DataStoreRepository
import com.fiction.domain.repository.LaunchInfoRepo
import com.fiction.domain.repository.TokenRepository
import com.fiction.domain.utils.Constants.Companion.ERROR_NULL_DATA
import com.fiction.entities.utils.Constants
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext

class GuestUseCaseImpl(
    private val tokenRepo: TokenRepository,
    private val dataStoreRepo: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider,
    private val launchInfoRepo: LaunchInfoRepo
) : GuestUseCase {

    override suspend fun invoke(uuId: String): ActionResult<Token> =
        withContext(dispatcher.io) {
            if (dataStoreRepo.getIsLogged() == true) {
                return@withContext ActionResult.Error(CallException(ERROR_NULL_DATA))
            }
            val isInstall = launchInfoRepo.getIsInstall(Constants.LAUNCH_INFO_STATUS_ID) ?: true
            when (val apiData = tokenRepo.getGuestToken(uuId, isInstall)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        val guestToken = Token.from(it)
                        dataStoreRepo.setToken(guestToken)
                        dataStoreRepo.setRefreshToken(it.refreshToken ?: "")
                        ActionResult.Success(guestToken)
                    } ?: ActionResult.Error(CallException(ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}