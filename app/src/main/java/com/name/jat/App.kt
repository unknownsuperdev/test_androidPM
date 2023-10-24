package com.name.jat

import android.app.Application
import com.name.jat.di.*
import com.name.di.apiModule
import com.name.di.databaseModule
import com.name.di.repositoryModule
import com.name.di.useCaseModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.*

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(modules)
        }
    }

    private val modules = listOf(
        apiModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        dispatcherModule
    )
}