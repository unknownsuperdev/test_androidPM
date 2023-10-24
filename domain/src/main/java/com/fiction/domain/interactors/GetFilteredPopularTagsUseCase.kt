package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.PopularTagsDataModel

interface GetFilteredPopularTagsUseCase {
    suspend operator fun invoke(): ActionResult<List<PopularTagsDataModel>>
}