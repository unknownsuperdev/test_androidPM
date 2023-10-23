package ru.tripster.guide

import android.app.Application
import android.content.Context
import cloud.mindbox.mindbox_firebase.MindboxFirebase
import cloud.mindbox.mobile_sdk.Mindbox
import cloud.mindbox.mobile_sdk.MindboxConfiguration
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.unloadKoinModules
import org.koin.core.logger.Level
import ru.tripster.di.apiModule
import ru.tripster.di.databaseModule
import ru.tripster.di.repositoryModule
import ru.tripster.di.useCaseModule
import ru.tripster.guide.di.appModule
import ru.tripster.guide.di.viewModelModule

class App : Application() {

    companion object {
        lateinit var appContext: Context

        //        private var scope: Scope? = null
        fun refreshScope() {
            unloadKoinModules(modules = listOf(apiModule, appModule))
            loadKoinModules(modules = listOf(apiModule, appModule))
        }
    }


    override fun onCreate() {
        super.onCreate()
        appContext = this
        startKoin {
            androidContext(applicationContext)
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            modules(modules)
        }

        val configuration = MindboxConfiguration.Builder(
            applicationContext,
            "api.mindbox.ru",
            "Tripster.GuideApp" )
            .shouldCreateCustomer(true)
            .subscribeCustomerIfCreated(true)
            .build()

        Mindbox.init(applicationContext, configuration, listOf(MindboxFirebase))
    }

    fun getInstance(): Application = this

    private val modules = listOf(
        appModule,
        apiModule,
        databaseModule,
        repositoryModule,
        useCaseModule,
        viewModelModule
    )
}








