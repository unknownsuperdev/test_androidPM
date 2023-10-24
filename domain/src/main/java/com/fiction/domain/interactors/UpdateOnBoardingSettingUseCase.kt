package com.fiction.domain.interactors

import com.fiction.core.ActionResult

interface UpdateOnBoardingSettingUseCase {
    suspend operator fun invoke(
        genderId: Int? = null,
        readingTimeId: Int? = null,
        favoriteTagIds: List<Long>? = null
    ): ActionResult<String>
}
