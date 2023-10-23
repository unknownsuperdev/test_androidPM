package ru.tripster.data.dataservice.apiservice.statistics


import retrofit2.Response
import retrofit2.http.GET
import ru.tripster.entities.responce.response.statistics.OrderStatisticsResponse

interface StatisticsApiService {
    @GET("guides/v2/info/statistics/")
    suspend fun getStatistics(): Response<OrderStatisticsResponse>
}