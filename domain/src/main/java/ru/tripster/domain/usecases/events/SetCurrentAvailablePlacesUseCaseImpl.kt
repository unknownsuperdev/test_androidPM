package ru.tripster.domain.usecases.events

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.events.SetCurrentAvailablePlacesUseCase
import ru.tripster.domain.model.events.EventResults
import ru.tripster.domain.repository.events.EventsRepository

class SetCurrentAvailablePlacesUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val eventsRepository: EventsRepository
) : SetCurrentAvailablePlacesUseCase {
    override suspend fun invoke(
        groupOrderId: Int,
        availablePlaces: Int,
        lastrVisitDate: String
    ): ActionResult<EventResults> = withContext(dispatcher.io) {
        when (val apiData =
            eventsRepository.setCurrentAvailablePlaces(
                groupOrderId,
                availablePlaces,
                lastrVisitDate
            )) {
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