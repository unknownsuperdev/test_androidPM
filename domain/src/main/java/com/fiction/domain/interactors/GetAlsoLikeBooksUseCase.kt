package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BooksDataModel
import com.fiction.domain.model.RetentionScreenBookItem

interface GetAlsoLikeBooksUseCase {
    suspend operator fun invoke(
        bookId: Long,
        isClearList: Boolean = false
    ): ActionResult<List<BooksDataModel>>
}