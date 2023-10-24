package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.LogoutUseCase
import com.fiction.domain.model.registration.Token
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.repository.DataStoreRepository
import com.fiction.domain.utils.Constants
import kotlinx.coroutines.withContext
class LogoutUseCaseImpl(
    private val authRepo: AuthRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : LogoutUseCase {
    override suspend operator fun invoke(): ActionResult<String> =
        withContext(dispatcher.io) {

            when (val apiData = authRepo.logout()) {
                is ActionResult.Success -> {
                    apiData.result.data?.let {
                        dataStoreRepository.setToken(Token("",""))
                        dataStoreRepository.setRefreshToken("")
                        ActionResult.Success(it.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}