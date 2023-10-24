package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.PopularTagsDataModel

interface GetPopularTagsUseCase {
    suspend operator fun invoke(isClear: Boolean = false): ActionResult<List<PopularTagsDataModel>>
}