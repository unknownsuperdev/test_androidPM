package com.name.data.repository

import com.name.core.ActionResult
import com.name.data.dataservice.apiservice.ExploreService
import com.name.data.util.analyzeResponse
import com.name.data.util.makeApiCall
import com.name.domain.repository.ExploreDataRepository
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.explore.ExploreDataItemResponse

class ExploreDataRepositoryImpl(private val exploreService: ExploreService) :
    ExploreDataRepository {

    override suspend fun getExploreData(): ActionResult<BaseResultModel<List<ExploreDataItemResponse>>> =
        makeApiCall {
            analyzeResponse(
                exploreService.getExploreData()
            )
        }
}