package com.analytics.kinesis.di

import com.analytics.api.AnalyticProvider
import com.analytics.kinesis.Constant
import com.analytics.kinesis.Constant.DI.COUNTRY_QUALIFIER
import com.analytics.kinesis.Constant.Network.COUNTRY_FIND_DOMAIN
import com.analytics.kinesis.data.provider.KinesisAnalyticProvider
import com.analytics.kinesis.data.network.CountryRestApi
import com.analytics.kinesis.data.repository.country.CountryApiRepository
import com.analytics.kinesis.data.repository.country.CountryApiRepositoryImpl
import com.analytics.kinesis.data.utils.device.DeviceUtilsProvider
import com.analytics.kinesis.data.utils.device.DeviceUtilsProviderImpl
import com.core.network.provider.ProviderRestApiDispatcher
import com.core.network.provider.ProviderRestApiDispatcherImpl
import org.koin.core.qualifier.named
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

fun loadAnalyticsModule(
    streamName: String,
    durationNewSession: Long = Constant.Session.DURATION_NEW_SESSION
) = module {
    single<AnalyticProvider> {
        KinesisAnalyticProvider(
            get(),
            streamName,
            durationNewSession,
            get()
        )
    }

    single<DeviceUtilsProvider> {
        DeviceUtilsProviderImpl(get(), get())
    }

    single<ProviderRestApiDispatcher> {
        ProviderRestApiDispatcherImpl()
    }

    single(named(COUNTRY_QUALIFIER)) {
        (get() as ProviderRestApiDispatcher).provideRestApiDispatcher(
            CountryRestApi::class,
            mutableListOf(COUNTRY_FIND_DOMAIN),
            mutableListOf()
        )
    }

    single<CountryApiRepository> {
        CountryApiRepositoryImpl(get(qualifier(COUNTRY_QUALIFIER)))
    }

}