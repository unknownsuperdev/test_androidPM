package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BooksWithReadProgressBookData

interface GetFinishedReadBooksUseCase {
    suspend operator fun invoke(isMakeCallAnyway: Boolean = false, isClear: Boolean = false): ActionResult<List<BooksWithReadProgressBookData>>
    fun getCacheData(): List<BooksWithReadProgressBookData>
}