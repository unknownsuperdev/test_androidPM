package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.LoginWithAppleUseCase
import com.fiction.domain.model.registration.Token
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.repository.DataStoreRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class LoginWithAppleImpl(
    private val authRepo: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : LoginWithAppleUseCase {
    override suspend operator fun invoke(
        uuId: String,
        identifier: String
    ): ActionResult<Token> =
        withContext(dispatcher.io) {
            when (val apiData = authRepo.loginWithApple(uuId, identifier)) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        val userToken = Token.from(it)
                        dataStoreRepository.setToken(userToken)
                        ActionResult.Success(Token.from(it))
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}