package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface LogoutUseCase {
    suspend operator fun invoke(): ActionResult<String>
}