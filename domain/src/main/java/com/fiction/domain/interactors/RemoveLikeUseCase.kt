package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface RemoveLikeUseCase {
    suspend operator fun invoke(
        bookId: Long,
        likeCount: Int
    ): ActionResult<String>
}