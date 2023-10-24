package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface BuyChapterUseCase {
    suspend operator fun invoke(chapterId: Long): ActionResult<Int?>
}