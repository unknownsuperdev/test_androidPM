package com.name.domain.interactors

import com.name.core.ActionResult

interface RemoveProfileAvatarUseCase {
    suspend operator fun invoke(): ActionResult<Any>
}