package ru.tripster.domain.usecases.events

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.events.EventCountersUseCase
import ru.tripster.domain.model.events.EventCounters
import ru.tripster.domain.repository.events.EventCountersRepository

class EventCountersUseCaseImpl ( private val dispatcher: CoroutineDispatcherProvider, private val eventCountersRepository: EventCountersRepository) :
    EventCountersUseCase {
    override suspend fun invoke(): ActionResult<EventCounters> = withContext(dispatcher.io) {
        when(val apiData = eventCountersRepository.getEventCounters()) {
            is ActionResult.Success -> {
                apiData.result.let {
                    ActionResult.Success(EventCounters.from(it))
                }
            }

            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}