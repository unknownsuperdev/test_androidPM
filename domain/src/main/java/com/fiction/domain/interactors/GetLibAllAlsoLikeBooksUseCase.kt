package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BookInfoAdapterModel

interface GetLibAllAlsoLikeBooksUseCase {
    suspend operator fun invoke(): ActionResult<List<BookInfoAdapterModel>>
}