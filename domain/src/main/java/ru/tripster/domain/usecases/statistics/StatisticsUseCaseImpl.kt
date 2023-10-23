package ru.tripster.domain.usecases.statistics

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.statistics.StatisticsUseCase
import ru.tripster.domain.model.statistics.OrderStatisticsData
import ru.tripster.domain.repository.statistics.StatisticsRepository

class StatisticsUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val statisticsRepository: StatisticsRepository
) :
    StatisticsUseCase {
    override suspend fun invoke(): ActionResult<OrderStatisticsData> = withContext(dispatcher.io) {
        when (val apiData = statisticsRepository.getStatistics()) {
            is ActionResult.Success -> {
                apiData.result.let {
                    ActionResult.Success(OrderStatisticsData.from(it))
                }
            }
            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}