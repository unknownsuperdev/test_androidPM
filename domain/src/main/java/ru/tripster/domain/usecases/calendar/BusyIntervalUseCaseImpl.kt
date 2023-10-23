package ru.tripster.domain.usecases.calendar

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.сalendar.BusyIntervalUseCase
import ru.tripster.domain.repository.сalendar.BusyIntervalRepository

class BusyIntervalUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val busyIntervalRepository: BusyIntervalRepository
) : BusyIntervalUseCase {
    override suspend fun invoke(id: Int): ActionResult<Boolean> = withContext(dispatcher.io) {
        when (val apiData = busyIntervalRepository.deleteBusyInterval(id)) {
            is ActionResult.Success -> {
                ActionResult.Success(true)
            }
            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}