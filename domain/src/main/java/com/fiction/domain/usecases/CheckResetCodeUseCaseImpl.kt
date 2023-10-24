package com.fiction.domain.usecases

import com.fiction.core.ActionResult
import com.fiction.core.CallException
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.CheckResetCodeUseCase
import com.fiction.domain.repository.AuthRepository
import com.fiction.domain.utils.Constants
import com.fiction.entities.response.registration.forgotpassword.CheckResetCodeRequest
import kotlinx.coroutines.withContext

class CheckResetCodeUseCaseImpl(
    private val authRepo: AuthRepository,
    private val dispatcher: CoroutineDispatcherProvider
) : CheckResetCodeUseCase {

    override suspend fun invoke(
        code: String,
        email: String
    ): ActionResult<String?> =
        withContext(dispatcher.io) {
            when (val apiData = authRepo.checkResetCode(CheckResetCodeRequest(code, email))) {
                is ActionResult.Success -> {
                    apiData.result.data?.let { response ->
                        ActionResult.Success(response.token)
                    } ?: ActionResult.Error(CallException(Constants.ERROR_NULL_DATA))
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}