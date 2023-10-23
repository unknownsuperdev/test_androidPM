package ru.tripster.domain.usecases.calendar

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.сalendar.CloseTimeUseCase
import ru.tripster.domain.model.calendar.CloseTimeSchedule
import ru.tripster.domain.model.calendar.CloseTimeSchedule.Companion.from
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.сalendar.CloseTimeRepository

class CloseTimeUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val closeTimeRepository: CloseTimeRepository,
    private val dataStoreRepository: DataStoreRepository
) : CloseTimeUseCase {
    override suspend fun invoke(
        closeTime: CloseTimeSchedule, experienceId: Int,
        experienceTitle: String
    ): ActionResult<Unit> =
        withContext(dispatcher.io) {
            dataStoreRepository.saveExperienceIdFiltration(experienceId)
            dataStoreRepository.saveExperienceTitleFiltration(experienceTitle)

            when (val apiData = closeTimeRepository.addCloseTime(closeTime.from())) {
                is ActionResult.Success -> {
                    ActionResult.Success(Unit)
                }

                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}