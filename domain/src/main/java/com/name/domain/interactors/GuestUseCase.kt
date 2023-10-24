package com.name.domain.interactors

import com.name.core.ActionResult
import com.name.domain.model.registration.GuestToken

interface GuestUseCase {
    suspend operator fun invoke(uuId: String): ActionResult<GuestToken>
}