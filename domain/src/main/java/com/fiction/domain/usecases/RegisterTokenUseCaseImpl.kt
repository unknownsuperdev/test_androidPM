package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.core.utils.Uuid
import com.fiction.domain.interactors.RegisterTokenUseCase
import com.fiction.domain.model.registration.Token
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.repository.DataStoreRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext

class RegisterTokenUseCaseImpl(
    private val authRepo: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val uuid: Uuid,
    private val dispatcher: CoroutineDispatcherProvider
) : RegisterTokenUseCase {

    override suspend fun invoke(
        email: String,
        password: String
    ): ActionResult<Token> =
        withContext(dispatcher.io) {
            when (val apiData = authRepo.getRegisterToken(uuid.getUuid(), email, password)) {
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