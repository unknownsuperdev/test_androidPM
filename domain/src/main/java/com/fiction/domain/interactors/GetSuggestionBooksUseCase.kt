package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.RetentionScreenBookItem

interface GetSuggestionBooksUseCase {
    suspend operator fun invoke(
        bookId: Long,
        isClearList: Boolean = false
    ): ActionResult<List<RetentionScreenBookItem>>
}