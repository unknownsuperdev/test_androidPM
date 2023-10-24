package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BooksWithReadProgressBookData


interface GetCurrentReadingBooksUseCase {
    suspend operator fun invoke(isMakeCallAnyway: Boolean = false, isClear: Boolean = false): ActionResult<List<BooksWithReadProgressBookData>>
    fun getCachedData(): List<BooksWithReadProgressBookData>
}
