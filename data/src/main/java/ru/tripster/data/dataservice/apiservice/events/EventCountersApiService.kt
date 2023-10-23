package ru.tripster.data.dataservice.apiservice.events

import retrofit2.Response
import retrofit2.http.GET
import ru.tripster.entities.response.events.EventCountersResponse

interface EventCountersApiService {
    @GET("guides/v2/events/counters/")
    suspend fun getEventCounters() : Response<EventCountersResponse>
}