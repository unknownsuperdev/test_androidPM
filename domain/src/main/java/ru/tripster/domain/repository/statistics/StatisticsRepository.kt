package ru.tripster.domain.repository.statistics

import ru.tripster.core.ActionResult
import ru.tripster.entities.responce.response.statistics.OrderStatisticsResponse

interface StatisticsRepository {
    suspend fun getStatistics(): ActionResult<OrderStatisticsResponse>
}