package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface GetBalanceUseCase {
    suspend operator fun invoke(): ActionResult<Int?>
}