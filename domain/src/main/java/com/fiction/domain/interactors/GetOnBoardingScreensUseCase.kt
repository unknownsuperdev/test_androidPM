package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface GetOnBoardingScreensUseCase {
    suspend operator fun invoke(): ActionResult<List<String>>
}
