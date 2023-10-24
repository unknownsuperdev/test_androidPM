package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.ForgotPasswordUseCase
import com.fiction.domain.model.registration.ForgotPassword
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.utils.Constants
import com.fiction.entities.response.registration.forgotpassword.ForgotPasswordRequest
import kotlinx.coroutines.withContext

class ForgotPasswordUseCaseImpl(
    private val authRepo: AuthRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : ForgotPasswordUseCase {

    override suspend fun invoke(email: String): ActionResult<ForgotPassword> =
        withContext(dispatcher.io) {
            when (val apiData = authRepo.forgotPassword(ForgotPasswordRequest(email))) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        val forgotPassword = ForgotPassword.from(response)
                        ActionResult.Success(forgotPassword)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
