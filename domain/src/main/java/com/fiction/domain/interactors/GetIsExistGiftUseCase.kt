package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface GetIsExistGiftUseCase {
    suspend operator fun invoke(): ActionResult<Boolean>
}