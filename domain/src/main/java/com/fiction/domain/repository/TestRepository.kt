package com.fiction.domain.repository

import com.fiction.core.ActionResult
import com.fiction.entities.response.BaseResultModel
import com.fiction.entities.response.DataInfoResponse

interface TestRepository {
    suspend fun getTestData(): ActionResult<BaseResultModel<List<DataInfoResponse>>>
}