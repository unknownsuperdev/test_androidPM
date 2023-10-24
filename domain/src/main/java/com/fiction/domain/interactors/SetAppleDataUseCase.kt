package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.analytic.AnalyticApple

interface SetAppleDataUseCase {
    suspend operator fun invoke(analyticApple: AnalyticApple): ActionResult<String>
}