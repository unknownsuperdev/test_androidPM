package com.name.domain.repository

import com.name.core.ActionResult
import com.name.entities.responce.BaseResultModel
import com.name.entities.responce.DataInfoResponse

interface TestRepository {
    suspend fun getTestData(): ActionResult<BaseResultModel<List<DataInfoResponse>>>
}