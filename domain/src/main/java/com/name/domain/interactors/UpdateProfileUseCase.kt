package com.name.domain.interactors

import com.name.core.ActionResult

interface UpdateProfileUseCase {
    suspend operator fun invoke(
        name: String? = null,
        avatarUri: String? = null,
        autoUnlockPaid: Boolean? = null,
        readingMode: Boolean? = null
    ): ActionResult<Any>
}