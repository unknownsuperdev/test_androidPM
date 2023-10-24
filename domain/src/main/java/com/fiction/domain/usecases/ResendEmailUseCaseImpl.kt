package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.GetUserTokenFromDatastoreUseCase
import com.fiction.domain.interactors.ResendEmailUseCase
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.utils.Constants
import com.fiction.entities.response.registration.verification.TokenRequest
import kotlinx.coroutines.withContext

class ResendEmailUseCaseImpl(
    private val authRepo: AuthRepository,
    private val datastoreUseCase: GetUserTokenFromDatastoreUseCase,
    private val dispatcher: CoroutineDispatcherProvider
) : ResendEmailUseCase {

    override suspend fun invoke(): ActionResult<String?> =
        withContext(dispatcher.io) {
            val token = datastoreUseCase()
            when (val apiData = authRepo.resendEmail(TokenRequest(token))) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        ActionResult.Success(response.message)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
