package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SetViewInBookUseCase {
    suspend operator fun invoke(bookId: Long): ActionResult<Any>
}