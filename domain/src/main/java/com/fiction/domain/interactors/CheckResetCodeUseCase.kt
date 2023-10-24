package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface CheckResetCodeUseCase {

    suspend operator fun invoke(
        code: String,
        email: String
    ): ActionResult<String?>
}
