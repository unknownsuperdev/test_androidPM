package ru.tripster.data.dataservice.apiservice.calendar

import retrofit2.Response
import retrofit2.http.*
import ru.tripster.entities.request.calendar.closeTime.CloseTimeRequest
import ru.tripster.entities.response.calendar.GuidesScheduleResponse
import ru.tripster.entities.response.calendar.dateOrder.GuideDateScheduleResponse


interface CalendarApiService {
    @GET("guides/v2/schedule/")
    suspend fun getGuidesSchedule(
        @Query("begin") begin: String,
        @Query("end") end: String,
        @Query("experience") experience: Int?,
        @Query("show_event_data") showEventData : Boolean? = null,
    ): Response<List<GuidesScheduleResponse>>

    @GET("guides/v2/schedule/{date}")
    suspend fun getGuidesDateSchedule(
        @Path("date") date: String,
        @Query("experience") experience: Int?
    ): Response<GuideDateScheduleResponse>

    @POST("guides/v2/schedule/add_busy_time/")
    suspend fun addBusySchedule(
        @Body closeTimeRequest: CloseTimeRequest
    ): Response<Unit>
}