package ru.tripster.data.repository

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.AllApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.TestRepository
import ru.tripster.entities.response.*

class TestRepositoryImpl(private val allApiService: AllApiService) : TestRepository {

    override suspend fun getTestData(): ActionResult<BaseResultModel<BaseDataModel>> =
        makeApiCall {
            analyzeResponse(allApiService.getTestData())
        }
}