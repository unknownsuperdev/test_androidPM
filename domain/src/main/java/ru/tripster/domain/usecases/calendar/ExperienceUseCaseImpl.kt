package ru.tripster.domain.usecases.calendar

import androidx.paging.PagingData
import androidx.paging.TerminalSeparatorType
import androidx.paging.insertHeaderItem
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import ru.tripster.domain.interactors.сalendar.ExperienceUseCase
import ru.tripster.domain.model.calendar.filtering.ExperienceResults
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.сalendar.ExperienceRepository
import ru.tripster.entities.response.calendar.filtering.ExperienceTitleResponseModel

class ExperienceUseCaseImpl(
    private val experienceRepository: ExperienceRepository,
    private val dataStoreRepository: DataStoreRepository
) : ExperienceUseCase {
    override suspend fun invoke(
        isVisible: Boolean,
        isClosingTime: Boolean
    ): Flow<PagingData<ExperienceResults>> {
        return experienceRepository.getExperience(isVisible).flow.map { pagingData ->
            pagingData.insertHeaderItem(
                TerminalSeparatorType.SOURCE_COMPLETE,
                ExperienceTitleResponseModel(
                    format = "",
                    id = 0,
                    is_visible = true,
                    movement_type = "",
                    tagline = "",
                    title = if (!isClosingTime) "Все заказы" else "По всем моим предложениям",
                    type = ""
                )
            ).map {
                ExperienceResults.from(it).apply {
                    if (this.id == dataStoreRepository.getExperienceIdFiltration().first() && !isClosingTime) {
                        this.isChecked = true
                    } else if (this.id == dataStoreRepository.getClosingTimeFiltrationId()
                            .first() && isClosingTime
                    ) this.isChecked = true
                }
            }
        }
    }
}