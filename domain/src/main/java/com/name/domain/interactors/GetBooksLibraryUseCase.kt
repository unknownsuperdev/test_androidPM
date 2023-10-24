package com.name.domain.interactors

import com.name.core.ActionResult
import com.name.domain.model.BooksWithReadProgressBookData

interface GetBooksLibraryUseCase {
    suspend operator fun invoke(): ActionResult<List<BooksWithReadProgressBookData>>
}