package com.name.data.repository

import com.name.core.ActionResult
import com.name.data.dataservice.apiservice.AllApiService
import com.name.data.util.analyzeResponse
import com.name.data.util.makeApiCall
import com.name.domain.repository.TestRepository
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.DataInfoResponse

class TestRepositoryImpl(private val allApiService: AllApiService) : TestRepository {

    override suspend fun getTestData(): ActionResult<BaseResultModel<List<DataInfoResponse>>> =
        makeApiCall {
            analyzeResponse(allApiService.getTestData())
        }
}