package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.PopularTagsModel

interface GetAllTagsUseCase {
    suspend operator fun invoke(): ActionResult<List<PopularTagsModel>>
}