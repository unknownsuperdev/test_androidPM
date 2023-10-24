package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.registration.Token

interface GuestUseCase {
    suspend operator fun invoke(uuId: String): ActionResult<Token>
}