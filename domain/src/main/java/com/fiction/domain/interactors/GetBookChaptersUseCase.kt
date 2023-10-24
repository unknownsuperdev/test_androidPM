package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.ChaptersDataModel

interface GetBookChaptersUseCase {
    suspend operator fun invoke(
        currentBookId: Long,
        lastReadChapterId: Long = -1L,
        isPurchased: Boolean? = null,
        isClear: Boolean = false
    ): ActionResult<List<ChaptersDataModel>>
}