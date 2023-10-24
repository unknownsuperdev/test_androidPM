package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SetAppsflyerDataUseCase {
    suspend operator fun invoke(appsflyerId: String?, idfa: String?, uniqueId: String?): ActionResult<String>
}