package com.name.domain.interactors

import com.name.core.ActionResult
import com.name.domain.model.DataInfo

interface TestUseCase {
    suspend operator fun invoke(): ActionResult<List<DataInfo>>
}