package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.TariffsItem

interface GetAvailableTariffsUseCase {
    suspend operator fun invoke(isClearList: Boolean = false, isMakeCallAnyway: Boolean = false): ActionResult<List<TariffsItem>>
}