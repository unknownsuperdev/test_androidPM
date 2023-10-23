package ru.tripster.data.repository.сalendar

import androidx.paging.Pager
import androidx.paging.PagingConfig
import ru.tripster.core.ActionResult
import ru.tripster.data.dataservice.apiservice.calendar.ExperienceApiService
import ru.tripster.data.dataservice.pagingSource.calendar.ExperiencePagingSource
import ru.tripster.data.util.analyzeResponse
import ru.tripster.data.util.makeApiCall
import ru.tripster.domain.model.calendar.filtering.Experience
import ru.tripster.domain.repository.сalendar.ExperienceRepository
import ru.tripster.entities.response.calendar.filtering.ExperienceResponse
import ru.tripster.entities.response.calendar.filtering.ExperienceTitleResponseModel

class ExperienceRepositoryImpl(private val experienceApiService: ExperienceApiService) :
    ExperienceRepository {
    override suspend fun getExperience(isVisible: Boolean): Pager<Int, ExperienceTitleResponseModel> =
        Pager(
            PagingConfig(
                20,
                enablePlaceholders = false,
                prefetchDistance = 1,
                initialLoadSize = 1
            )
        ) {
            ExperiencePagingSource.Factory().create(experienceApiService, isVisible)
        }

    override suspend fun getExperienceFullData(isVisible: Boolean): ActionResult<ExperienceResponse> = makeApiCall {
        analyzeResponse(experienceApiService.getExperience(isVisible,1,20))
    }
}