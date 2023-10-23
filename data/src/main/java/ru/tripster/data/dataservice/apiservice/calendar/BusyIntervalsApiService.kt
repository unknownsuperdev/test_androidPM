package ru.tripster.data.dataservice.apiservice.calendar

import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.Path

interface BusyIntervalsApiService {
    @DELETE("guides/v2/busy-intervals/{id}/")
    suspend fun deleteBusyInterval(
        @Path("id") id: Int
    ): Response<Unit>
}