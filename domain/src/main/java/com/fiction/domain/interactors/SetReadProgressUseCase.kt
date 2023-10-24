package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SetReadProgressUseCase {
    suspend operator fun invoke(
        chapterId: Long,
        percent: Double
    ): ActionResult<Any>
}