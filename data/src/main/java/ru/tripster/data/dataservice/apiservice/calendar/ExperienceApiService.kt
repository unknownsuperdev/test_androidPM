package ru.tripster.data.dataservice.apiservice.calendar

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.tripster.entities.response.calendar.filtering.ExperienceResponse

interface ExperienceApiService {
    @GET("guides/v2/experiences/")
    suspend fun getExperience(
        @Query("is_visible") isVisible: Boolean,
        @Query("page") page: Int,
        @Query("page_size") pageSize: Int,
        @Query("search") search: String? = null,
    ): Response<ExperienceResponse>
}