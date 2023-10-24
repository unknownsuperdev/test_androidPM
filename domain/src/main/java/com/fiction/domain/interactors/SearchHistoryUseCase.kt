package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.SearchMainItem

interface SearchHistoryUseCase {
    suspend operator fun invoke(): ActionResult<List<SearchMainItem>>
}