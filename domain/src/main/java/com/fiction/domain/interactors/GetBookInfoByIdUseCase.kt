package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BookSummaryInfo

interface GetBookInfoByIdUseCase {
    suspend operator fun invoke(bookId: Long): ActionResult<BookSummaryInfo>
}