package ru.tripster.guide.di

import org.koin.dsl.module
import ru.tripster.di.BuildConfig
import ru.tripster.guide.analytics.Analytic
import ru.tripster.guide.analytics.AnalyticImpl
import ru.tripster.guide.analytics.AnalyticsConstants.API_KEY_AMPLITUDE_DEV
import ru.tripster.guide.analytics.AnalyticsConstants.API_KEY_AMPLITUDE_PROD
import ru.tripster.guide.analytics.ChangedApiKey

val appModule = module {
    single<Analytic> {
        AnalyticImpl(
            get(), when (ChangedApiKey.isProdAnalytics) {
                null -> {
                    if (BuildConfig.DEBUG) API_KEY_AMPLITUDE_DEV else API_KEY_AMPLITUDE_PROD
                }
                else -> {
                    if (ChangedApiKey.isProdAnalytics == true) API_KEY_AMPLITUDE_PROD else API_KEY_AMPLITUDE_DEV
                }
            }
        )
    }
}