package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.registration.Token

interface LoginWithGoogleUseCase {
    suspend operator fun invoke(uuId: String, identifier: String): ActionResult<Token>
}