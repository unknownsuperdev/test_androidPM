package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.SearchMainItem

interface SearchBookUseCase {

    suspend operator fun invoke(word: String): ActionResult<List<SearchMainItem>>
}
