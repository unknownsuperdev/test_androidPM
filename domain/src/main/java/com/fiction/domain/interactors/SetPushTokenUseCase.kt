package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SetPushTokenUseCase{
    suspend operator fun invoke(
        pushToken: String,
    ): ActionResult<String>
}