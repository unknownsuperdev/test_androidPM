package ru.tripster.domain.usecases.calendar

import kotlinx.coroutines.withContext
import ru.tripster.core.ActionResult
import ru.tripster.core.dispatcher.CoroutineDispatcherProvider
import ru.tripster.domain.interactors.сalendar.GetGuidesScheduleUseCase
import ru.tripster.domain.model.MenuItems
import ru.tripster.domain.model.calendar.GuidesSchedule
import ru.tripster.domain.model.calendar.GuidesSchedule.Companion.from
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.сalendar.CalendarRepository

class GetGuidesScheduleUseCaseImpl(
    private val dispatcher: CoroutineDispatcherProvider,
    private val calendarRepository: CalendarRepository,
    private val dataStoreRepository: DataStoreRepository
) : GetGuidesScheduleUseCase {
    override suspend fun invoke(
        begin: String,
        end: String,
        experienceId: Int?,
        experienceTitle:String?,
        showEventData: Boolean?
    ): ActionResult<List<GuidesSchedule>> = withContext(dispatcher.io) {
        dataStoreRepository.saveExperienceIdFiltration(experienceId ?: 0)
        dataStoreRepository.saveExperienceTitleFiltration(experienceTitle ?: "")
        dataStoreRepository.saveMenuItem(MenuItems.CALENDAR.type)

        when (val apiData = calendarRepository.getGuidesSchedule(
            begin,
            end,
            if (experienceId != 0) experienceId else null,
            showEventData
        )) {
            is ActionResult.Success -> {
                apiData.result.let {
                    ActionResult.Success(it.map { schedule ->
                        schedule.from()
                    })
                }
            }
            is ActionResult.Error -> {
                ActionResult.Error(apiData.errors)
            }
        }
    }
}
