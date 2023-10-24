package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface ResendEmailUseCase {
    suspend operator fun invoke(): ActionResult<String?>
}