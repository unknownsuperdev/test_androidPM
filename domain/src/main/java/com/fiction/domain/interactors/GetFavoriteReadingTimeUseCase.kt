package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.onboarding.FavoriteReadingTime

interface GetFavoriteReadingTimeUseCase {
    suspend operator fun invoke(): ActionResult<List<FavoriteReadingTime>>
}
