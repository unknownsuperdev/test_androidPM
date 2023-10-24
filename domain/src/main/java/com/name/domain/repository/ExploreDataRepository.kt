package com.name.domain.repository

import com.name.core.ActionResult
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.ExploreDataItemResponse


interface ExploreDataRepository {

    suspend fun getExploreData(): ActionResult<BaseResultModel<List<ExploreDataItemResponse>>>
}