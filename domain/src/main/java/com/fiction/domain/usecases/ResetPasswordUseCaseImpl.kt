package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.ResetPasswordUseCase
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.utils.Constants
import com.fiction.entities.response.registration.forgotpassword.ResetPasswordRequest
import kotlinx.coroutines.withContext

class ResetPasswordUseCaseImpl(
    private val authRepo: AuthRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ResetPasswordUseCase {

    override suspend fun invoke(
        password: String,
        token: String
    ): ActionResult<String?> =

        withContext(dispatcher.io) {
            when (val apiData = authRepo.resetPassword(ResetPasswordRequest(password, token))) {
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
