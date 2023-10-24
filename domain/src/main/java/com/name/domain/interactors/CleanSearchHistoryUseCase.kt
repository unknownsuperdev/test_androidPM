package com.name.domain.interactors

import com.name.core.ActionResult

interface CleanSearchHistoryUseCase {
    suspend operator fun invoke(): ActionResult<Any>
}