package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.BookInfoAdapterModel

interface GetMostPopularBooksUseCase {
    suspend operator fun invoke(
        id: Long = -1L,
        isSaved: Boolean? = null,
        isLiked: Boolean? = null,
        likesCount: Int? = null,
        isClear: Boolean = false
    ): ActionResult<List<BookInfoAdapterModel>>
}