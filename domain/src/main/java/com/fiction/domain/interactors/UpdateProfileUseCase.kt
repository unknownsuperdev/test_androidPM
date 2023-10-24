package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface UpdateProfileUseCase {
    suspend operator fun invoke(
        name: String? = null,
        avatarUri: String? = null,
        autoUnlockPaid: Boolean? = null,
        readingMode: Boolean? = null
    ): ActionResult<Any>
}