package ru.tripster.domain.interactors.statistics

import ru.tripster.core.ActionResult
import ru.tripster.domain.model.statistics.OrderStatisticsData

interface StatisticsUseCase {
    suspend operator fun invoke(): ActionResult<OrderStatisticsData>
}