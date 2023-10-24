package com.name.domain.interactors

import com.name.core.ActionResult
import com.name.domain.model.BaseExploreDataModel

interface ExploreDataUseCase{
    suspend operator fun invoke(): ActionResult<List<BaseExploreDataModel>>
}