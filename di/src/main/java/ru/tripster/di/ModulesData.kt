package ru.tripster.di

import android.app.Application
import androidx.room.Room
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tripster.data.BuildConfig
import ru.tripster.data.dataservice.apiservice.AllApiService
import ru.tripster.data.dataservice.apiservice.AuthService
import ru.tripster.data.dataservice.apiservice.calendar.BusyIntervalsApiService
import ru.tripster.data.dataservice.apiservice.calendar.CalendarApiService
import ru.tripster.data.dataservice.apiservice.calendar.ExperienceApiService
import ru.tripster.data.dataservice.apiservice.chat.ChatApiService
import ru.tripster.data.dataservice.apiservice.confirmOrEdit.OrderConfirmOrEditService
import ru.tripster.data.dataservice.apiservice.events.EventCountersApiService
import ru.tripster.data.dataservice.apiservice.events.EventsApiService
import ru.tripster.data.dataservice.apiservice.order.OrdersApiService
import ru.tripster.data.dataservice.apiservice.profile.ProfileApiService
import ru.tripster.data.dataservice.apiservice.statistics.StatisticsApiService
import ru.tripster.data.dataservice.appservice.DataStoreService
import ru.tripster.data.dataservice.appservice.DataStoreServiceImpl
import ru.tripster.data.dataservice.sqlservice.AppDatabase
import ru.tripster.data.repository.DataStoreRepositoryImpl
import ru.tripster.data.repository.LoginRepositoryImpl
import ru.tripster.data.repository.TestRepositoryImpl
import ru.tripster.data.repository.chat.OrderCommentsRepositoryImpl
import ru.tripster.data.repository.confirmOrEdit.OrderConfirmRepositoryImpl
import ru.tripster.data.repository.events.EventCountersRepositoryImpl
import ru.tripster.data.repository.events.EventsRepositoryImpl
import ru.tripster.data.repository.order.OrdersRepositoryImpl
import ru.tripster.data.repository.profile.ProfileRepositoryImpl
import ru.tripster.data.repository.statistics.StatisticsRepositoryImpl
import ru.tripster.data.repository.сalendar.BusyIntervalRepositoryImpl
import ru.tripster.data.repository.сalendar.CalendarRepositoryImpl
import ru.tripster.data.repository.сalendar.CloseTimeRepositoryImpl
import ru.tripster.data.repository.сalendar.ExperienceRepositoryImpl
import ru.tripster.data.util.HeaderInterceptor
import ru.tripster.di.BaseUrlUtil.baseUrl
import ru.tripster.domain.repository.DataStoreRepository
import ru.tripster.domain.repository.LoginRepository
import ru.tripster.domain.repository.TestRepository
import ru.tripster.domain.repository.chat.OrderCommentsRepository
import ru.tripster.domain.repository.events.EventCountersRepository
import ru.tripster.domain.repository.events.EventsRepository
import ru.tripster.domain.repository.events.OrderConfirmOrEditRepository
import ru.tripster.domain.repository.order.OrderDetailsRepository
import ru.tripster.domain.repository.profile.ProfileRepository
import ru.tripster.domain.repository.statistics.StatisticsRepository
import ru.tripster.domain.repository.сalendar.BusyIntervalRepository
import ru.tripster.domain.repository.сalendar.CalendarRepository
import ru.tripster.domain.repository.сalendar.CloseTimeRepository
import ru.tripster.domain.repository.сalendar.ExperienceRepository

val apiModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(baseUrl ?: BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .apply {
                client(
                    OkHttpClient.Builder()
                        .addInterceptor(HeaderInterceptor(get()))
                        .addInterceptor(HttpLoggingInterceptor().apply {
                            level = HttpLoggingInterceptor.Level.BODY
                        })
                        .build()
                )
            }
            .build()
    }
    single<AllApiService> { get<Retrofit>().create(AllApiService::class.java) }
    single<AuthService> { get<Retrofit>().create(AuthService::class.java) }
    single<EventsApiService> { get<Retrofit>().create(EventsApiService::class.java) }
    single<OrdersApiService> { get<Retrofit>().create(OrdersApiService::class.java) }
    single<ChatApiService> { get<Retrofit>().create(ChatApiService::class.java) }
    single<OrderConfirmOrEditService> { get<Retrofit>().create(OrderConfirmOrEditService::class.java) }
    single<EventCountersApiService> { get<Retrofit>().create(EventCountersApiService::class.java) }
    single<CalendarApiService> { get<Retrofit>().create(CalendarApiService::class.java) }
    single<StatisticsApiService> { get<Retrofit>().create(StatisticsApiService::class.java) }
    single<ProfileApiService> { get<Retrofit>().create(ProfileApiService::class.java) }
    single<ExperienceApiService> { get<Retrofit>().create(ExperienceApiService::class.java) }
    single<BusyIntervalsApiService> { get<Retrofit>().create(BusyIntervalsApiService::class.java) }
}

val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "TripsterDB")
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }

    single { provideDatabase(androidApplication()) }
    single { get<AppDatabase>().orderCommentsDao() }
}

val repositoryModule = module {
    single<DataStoreService> { DataStoreServiceImpl(get()) }
    factory<TestRepository> { TestRepositoryImpl(get()) }
    factory<EventsRepository> { EventsRepositoryImpl(get()) }
    factory<OrderDetailsRepository> { OrdersRepositoryImpl(get()) }
    factory<EventCountersRepository> { EventCountersRepositoryImpl(get()) }
//    single<LoginRepository> { LoginRepositoryImpl(get()) }
//    single<DataStoreRepository> { DataStoreRepositoryImpl(get()) }
//    single<OrderConfirmOrEditRepository> { OrderConfirmRepositoryImpl(get()) }
//    single<CalendarRepository> { CalendarRepositoryImpl(get()) }
//    single<StatisticsRepository> { StatisticsRepositoryImpl(get()) }
//    single<OrderCommentsRepository> { OrderCommentsRepositoryImpl(get(), get()) }
//    single<ProfileRepository> { ProfileRepositoryImpl(get()) }
//    single<ExperienceRepository> { ExperienceRepositoryImpl(get()) }
//    single<BusyIntervalRepository> { BusyIntervalRepositoryImpl(get()) }
//    single<CloseTimeRepository> { CloseTimeRepositoryImpl(get()) }
    factory<CloseTimeRepository> { CloseTimeRepositoryImpl(get()) }
    factory<LoginRepository> { LoginRepositoryImpl(get()) }
    factory<DataStoreRepository> { DataStoreRepositoryImpl(get()) }
    factory<OrderConfirmOrEditRepository> { OrderConfirmRepositoryImpl(get()) }
    factory<CalendarRepository> { CalendarRepositoryImpl(get()) }
    factory<StatisticsRepository> { StatisticsRepositoryImpl(get()) }
    factory<OrderCommentsRepository> { OrderCommentsRepositoryImpl(get(), get()) }
    factory<ProfileRepository> { ProfileRepositoryImpl(get()) }
    factory<ExperienceRepository> { ExperienceRepositoryImpl(get()) }
    factory<BusyIntervalRepository> { BusyIntervalRepositoryImpl(get()) }
}
