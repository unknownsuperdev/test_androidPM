package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SetAndroidDataUseCase {
    suspend operator fun invoke(gaid: String): ActionResult<String>
}