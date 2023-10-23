package ru.tripster.data.repository.statistics

import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.statistics.StatisticsApiService
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.repository.statistics.StatisticsRepository
import ru.tripster.entities.responce.response.statistics.OrderStatisticsResponse

class StatisticsRepositoryImpl(private val statisticsApiService: StatisticsApiService) :
    StatisticsRepository {
    override suspend fun getStatistics(): ActionResult<OrderStatisticsResponse> = makeApiCall {
        analyzeResponse(statisticsApiService.getStatistics())
    }
}