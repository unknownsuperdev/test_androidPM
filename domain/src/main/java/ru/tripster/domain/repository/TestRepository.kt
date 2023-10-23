package ru.tripster.domain.repository

import ru.tripster.core.ActionResult
import ru.tripster.entities.response.*

interface TestRepository {
    suspend fun getTestData(): ActionResult<BaseResultModel<BaseDataModel>>
}