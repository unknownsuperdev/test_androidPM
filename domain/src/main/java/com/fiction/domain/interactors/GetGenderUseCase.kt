package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.onboarding.Gender

interface GetGenderUseCase {
    suspend operator fun invoke(): ActionResult<List<Gender>>
}
