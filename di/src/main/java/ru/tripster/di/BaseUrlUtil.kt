package ru.tripster.di

import ru.tripster.data.BuildConfig

object BaseUrlUtil {
    var baseUrl: String? = null
    var currentBaseUrl: String = BuildConfig.BASE_URL
}

