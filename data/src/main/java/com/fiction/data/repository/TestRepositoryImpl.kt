package com.fiction.data.repository

import com.fiction.core.ActionResult
import com.fiction.data.dataservice.apiservice.AllApiService
import com.fiction.data.util.analyzeResponse
import com.fiction.data.util.makeApiCall
import com.fiction.domain.repository.TestRepository
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.DataInfoResponse

class TestRepositoryImpl(private val allApiService: AllApiService) : TestRepository {

    override suspend fun getTestData(): ActionResult<BaseResultModel<List<DataInfoResponse>>> =
        makeApiCall {
            analyzeResponse(allApiService.getTestData())
        }
}