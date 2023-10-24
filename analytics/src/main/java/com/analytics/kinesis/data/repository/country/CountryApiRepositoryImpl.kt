package com.analytics.kinesis.data.repository.country

import com.analytics.kinesis.data.model.responce.CountryData
import com.analytics.kinesis.data.network.CountryRestApi
import com.core.network.rest.dispatcher.RestApiDispatcher
import com.core.network.rest.repository.convertToDomain
import com.core.network.rest.response.model.ApiResponse

internal class CountryApiRepositoryImpl(
    private val restDispatcher: RestApiDispatcher<CountryRestApi>
) : CountryApiRepository {
    override suspend fun getCountryIp(): ApiResponse<CountryData> {
        return restDispatcher.executeRequest { api ->
            convertToDomain(
                api.getCountryIp()
            ) {
                it
            }
        }
    }
}