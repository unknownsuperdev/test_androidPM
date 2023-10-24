package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.gift.WelcomeGift

interface GetGiftUseCase {
    suspend operator fun invoke(): ActionResult<WelcomeGift>
}