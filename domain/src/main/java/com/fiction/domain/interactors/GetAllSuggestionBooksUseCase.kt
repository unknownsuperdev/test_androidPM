package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BookInfoAdapterModel

interface GetAllSuggestionBooksUseCase {
    suspend operator fun invoke(bookId: Long): ActionResult<List<BookInfoAdapterModel>>
}