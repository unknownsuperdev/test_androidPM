package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface ResetPasswordUseCase {
    suspend operator fun invoke(
        password: String,
        token: String
    ): ActionResult<String?>
}