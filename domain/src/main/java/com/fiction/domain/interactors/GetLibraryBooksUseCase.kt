package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BooksWithReadProgressBookData

interface GetLibraryBooksUseCase {
    suspend operator fun invoke(
        isMakeCallAnyway: Boolean = false,
        isClear: Boolean = false
    ): ActionResult<List<BooksWithReadProgressBookData>>
}