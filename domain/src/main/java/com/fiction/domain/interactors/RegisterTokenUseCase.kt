package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.registration.Token

interface RegisterTokenUseCase {

    suspend operator fun invoke(
        email: String,
        password: String
    ): ActionResult<Token>
}