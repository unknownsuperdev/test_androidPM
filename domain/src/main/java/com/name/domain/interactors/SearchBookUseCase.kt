package com.name.domain.interactors

import com.name.core.ActionResult
import com.name.domain.model.SearchMainItem

interface SearchBookUseCase {

    suspend operator fun invoke(word: String): ActionResult<List<SearchMainItem>>
}
