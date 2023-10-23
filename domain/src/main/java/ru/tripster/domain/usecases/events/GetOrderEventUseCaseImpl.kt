package ru.tripster.domain.usecases.events

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.events.GetOrderEventUseCase
import ru.tripster.domain.model.events.EventResults
import ru.tripster.domain.repository.events.EventsRepository

class GetOrderEventUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val eventRepository: EventsRepository
) : GetOrderEventUseCase {
    override suspend fun invoke(orderId: Int): ActionResult<EventResults> =
        withContext(dispatcher.io) {
            when (val apiData = eventRepository.getOrderEvent(orderId)) {
                is ActionResult.Success -> {
                    apiData.result.let {
                        ActionResult.Success(EventResults.from(it))
                    }
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
