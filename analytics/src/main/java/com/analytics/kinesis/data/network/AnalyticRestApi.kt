package com.analytics.kinesis.data.network

import com.analytics.kinesis.data.model.responce.CountryData
import retrofit2.http.GET
import retrofit2.http.Headers

internal interface CountryRestApi {

    @Headers("Content-Type: application/json")
    @GET("json/")
    suspend fun getCountryIp(): CountryData

}