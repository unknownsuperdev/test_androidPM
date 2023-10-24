package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.onboarding.FavoriteThemeTags

interface GetFavoriteTagsUseCase {
    suspend operator fun invoke(): ActionResult<List<FavoriteThemeTags>>
}
