package com.name.domain.interactors

import com.name.core.ActionResult
import com.name.domain.model.SearchMainItem

interface SearchHistoryUseCase {
    suspend operator fun invoke(): ActionResult<List<SearchMainItem>>
}