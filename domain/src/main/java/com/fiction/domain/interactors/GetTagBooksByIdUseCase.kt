package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BookInfoAdapterModelList

interface GetTagBooksByIdUseCase {
    suspend operator fun invoke(tagId: Long): ActionResult<BookInfoAdapterModelList>
}