package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface CleanSearchHistoryUseCase {
    suspend operator fun invoke(): ActionResult<String>
}