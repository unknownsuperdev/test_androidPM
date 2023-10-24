package com.analytics.kinesis.data.repository.country

import com.analytics.kinesis.data.model.responce.CountryData
import com.core.network.rest.response.model.ApiResponse

internal interface CountryApiRepository {

    suspend fun getCountryIp(): ApiResponse<CountryData>
}