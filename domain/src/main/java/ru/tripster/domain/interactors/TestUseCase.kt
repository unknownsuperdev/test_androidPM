package ru.tripster.domain.interactors

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.DataInfo

interface TestUseCase {
    suspend operator fun invoke(): ActionResult<List<DataInfo>>
}