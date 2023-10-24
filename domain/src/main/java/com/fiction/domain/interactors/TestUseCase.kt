package com.fiction.domain.interactors

import com.fiction.core.ActionResult
import com.fiction.domain.model.DataInfo

interface TestUseCase {
    suspend operator fun invoke(): ActionResult<List<DataInfo>>
}