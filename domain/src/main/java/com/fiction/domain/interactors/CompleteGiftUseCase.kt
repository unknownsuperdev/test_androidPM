package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface CompleteGiftUseCase {
    suspend operator fun invoke(eventIds: List<Int>, giftCount: Int): ActionResult<String>
}