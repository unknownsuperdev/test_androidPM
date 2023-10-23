package ru.tripster.domain.usecases.calendar

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.сalendar.GetGuideDateScheduleUseCase
import ru.tripster.domain.model.calendar.GuideDateSchedule
import ru.tripster.domain.model.calendar.GuideDateSchedule.Companion.from
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.сalendar.CalendarRepository
import ru.tripster.entities.response.calendar.dateOrder.DateScheduleItemResponse
import ru.tripster.entities.response.calendar.dateOrder.GuideDateScheduleResponse

class GetGuideDateScheduleUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val calendarRepository: CalendarRepository,
    private val dataStoreRepository: DataStoreRepository
) : GetGuideDateScheduleUseCase {
    var listOfModels: List<DateScheduleItemResponse>? = listOf()
    override suspend fun invoke(
        date: String,
        experienceId: Int?,
        experienceTitle: String?
    ): ActionResult<GuideDateSchedule> =
        withContext(dispatcher.io) {
            dataStoreRepository.saveExperienceIdFiltration(experienceId ?: 0)
            dataStoreRepository.saveExperienceTitleFiltration(experienceTitle ?: "")

            when (val apiData = calendarRepository.getGuideDateSchedule(
                date,
                if (experienceId != 0) experienceId else null
            )) {
                is ActionResult.Success -> {
                    apiData.result.let {
                        it.let { response -> listOfModels = response.guideScheduleItemResponses }
                        val sortedModels = listOfModels?.sortedWith { model1, model2 ->
                            model2.time?.let { hour -> model1.time?.compareTo(hour) } ?: 0
                        }

                        val sortedItemResponse = GuideDateScheduleResponse(
                            it.date,
                            it.event_count,
                            it.event_total_count,
                            sortedModels
                        )
                        ActionResult.Success(sortedItemResponse.from().apply {
                            this.experienceId =
                                dataStoreRepository.getExperienceIdFiltration().first() ?: 0
                        })
                    }
                }
                is ActionResult.Error -> {
                    ActionResult.Error(apiData.errors)
                }
            }
        }
}
