package ru.tripster.domain.usecases.events

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.events.GetCurrentGteEventUseCase
import ru.tripster.domain.model.events.EventResults
import ru.tripster.domain.repository.events.EventsRepository

class GetCurrentGteEventUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val eventsRepository: EventsRepository
) : GetCurrentGteEventUseCase {
    override suspend fun invoke(
        groupOrderId: Int,
        guideLastVisitDate: String
    ): ActionResult<EventResults> = withContext(dispatcher.io) {
        when (val apiData =
            eventsRepository.getEventsDateCurrentGte(groupOrderId, guideLastVisitDate)) {
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