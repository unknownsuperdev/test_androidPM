package com.fiction.me

import android.app.Application
import com.analytics.kinesis.di.loadAnalyticsModule
import com.fiction.di.*
import com.fiction.domain.utils.NetworkMonitor
import com.fiction.me.di.*
import com.fiction.me.ui.fragments.dataProvider.IAppsFlyer
import com.google.firebase.FirebaseApp
import org.koin.android.BuildConfig
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class App : Application() {
    var attribution = ""
    private val appsFlyer: IAppsFlyer by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(modules)
        }
        appsFlyer.init()
        FirebaseApp.initializeApp(applicationContext)
        NetworkMonitor(this).startNetworkCallback()
    }

    private val modules = listOf(
        apiModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        viewModelModule,
        dispatcherModule,
        coreModule,
        preferenceModule,
        amplitudeModule,
        billingModule,
        notificationModule,
        appsFlyerModule,
        authModule,
        advertisingModule,
        viewModelModuleAuthorization,
        loadAnalyticsModule(
            com.fiction.me.BuildConfig.KINESIS_STREAM_NAME,
        )
    )

    override fun onTerminate() {
        super.onTerminate()
        NetworkMonitor(this).stopNetworkCallback()
    }

    companion object {
        const val LOG_TAG = "AppsFlyerFeedMeApp"
        const val DL_ATTRS = "dl_attrs"
        const val ATTRIBUTION = "attribution"
    }
}