package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.registration.ForgotPassword

interface ForgotPasswordUseCase {

    suspend operator fun invoke(
        email: String
    ): ActionResult<ForgotPassword>
}