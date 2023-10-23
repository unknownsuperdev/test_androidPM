package ru.tripster.data.dataservice.apiservice.events

import retrofit2.Response
import retrofit2.http.*
import ru.tripster.entities.responce.response.events.NumberOfPersonsAvailRequest
import ru.tripster.entities.response.events.EventsResponse
import ru.tripster.entities.response.events.ResultModel

interface EventsApiService {

    @GET("guides/v2/events/")
    suspend fun getEvents(
        @Query("is_in_work") isInWork: Boolean = true,
        @Query("sorting") sorting: String = "unread_first",
        @Query("page") page: Int = 1
    ): Response<EventsResponse>

    @GET("guides/v2/events/")
    suspend fun getEventsUnread(
        @Query("unread_comments_count__gt") unreadCommCount: Int = 0,
        @Query("sorting") sorting: String = "-last_modified_at",
        @Query("page") page: Int = 1
    ): Response<EventsResponse>

    @GET("guides/v2/events/")
    suspend fun getEventsStatusSorting(
        @Query("order__status") status: Int,
        @Query("sorting") sorting: String,
        @Query("page") page: Int = 1
    ): Response<EventsResponse>

    @GET("guides/v2/events/")
    suspend fun getEventsDoubleStatusSorting(
        @Query("order__status") status1: Int,
        @Query("order__status") status2: Int,
        @Query("sorting") sorting: String,
        @Query("page") page: Int = 1
    ): Response<EventsResponse>

    @GET("guides/v2/events/")
    suspend fun getEventsDateGte(
        @Query("order__status") status: Int,
        @Query("sorting") sorting: String,
        @Query("starts_at__gte") startsAt: String,
        @Query("page") page: Int = 1
    ): Response<EventsResponse>

    @GET("guides/v2/events/")
    suspend fun getEventsDateLte(
        @Query("order__status") status: Int,
        @Query("sorting") sorting: String,
        @Query("starts_at__lte") startsAt: String,
        @Query("page") page: Int = 1
    ): Response<EventsResponse>

    @GET("guides/v2/events/{id}")
    suspend fun getOrderEvent(
        @Path("id") id: Int
    ): Response<ResultModel>

    @PATCH("guides/v2/events/{id}/")
    suspend fun getEventsDateCurrentGte(
        @Path("id") id: Int,
        @Body guideLastVisitDate: NumberOfPersonsAvailRequest
    ): Response<ResultModel>

    @PUT("guides/v2/events/{id}/")
    suspend fun setEventAvailablePlaces(
        @Path("id") id: Int,
        @Body numberOfPersonsAvail: NumberOfPersonsAvailRequest
    ): Response<ResultModel>
}