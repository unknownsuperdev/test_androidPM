package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface SetLikeUseCase {
    suspend operator fun invoke(
        bookId: Long,
        likeCount: Int
    ): ActionResult<String>
}