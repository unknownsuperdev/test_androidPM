package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.analytic.AnalyticAdjust

interface SetAdjustDataUseCase {
    suspend operator fun invoke(analyticAdjust: AnalyticAdjust): ActionResult<String>
}