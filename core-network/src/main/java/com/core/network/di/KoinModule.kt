package com.core.network.di

import com.core.network.rest.RestProvider
import com.core.network.rest.retrofit.RetrofitRestProvider
import com.core.network.rest.utils.NetworkUtils
import org.koin.dsl.module

internal val module = module {

    single<RestProvider> {
        RetrofitRestProvider()
    }

    single {
        NetworkUtils(get())
    }

}