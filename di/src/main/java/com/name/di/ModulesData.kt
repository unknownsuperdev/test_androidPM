package com.name.di

import android.app.Application
import androidx.room.Room
import com.name.data.BuildConfig
import com.name.data.dataservice.apiservice.*
import com.name.data.dataservice.appservice.DataStoreService
import com.name.data.dataservice.appservice.DataStoreServiceImpl
import com.name.data.dataservice.sqlservice.AppDatabase
import com.name.data.repository.*
import com.name.data.util.HeaderInterceptor
import com.name.domain.repository.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
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
    single<ExploreService> { get<Retrofit>().create(ExploreService::class.java) }
    single<LibraryService> { get<Retrofit>().create(LibraryService::class.java) }
    single<BookStateService> { get<Retrofit>().create(BookStateService::class.java) }
    single<SearchService> { get<Retrofit>().create(SearchService::class.java) }
    single<ProfileService> { get<Retrofit>().create(ProfileService::class.java) }
}
val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "JatDB")
            // .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
    single { provideDatabase(androidApplication()) }
    single { get<AppDatabase>().settingsDao() }
}
val repositoryModule = module {
    single<TestRepository> { TestRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(get()) }
    single<ExploreDataRepository> { ExploreDataRepositoryImpl(get()) }
    single<SearchBookRepository> { SearchBookRepositoryImpl(get()) }
    single<DataStoreService> { DataStoreServiceImpl(get()) }
    single<BookSettingsRepo> { BookSettingsRepoImpl(get()) }
    single<LibraryRepository> { LibraryRepositoryImpl(get()) }
    single<BookStateRepo> { BookStateRepoImpl(get()) }
    single<ProfileRepo> { ProfileRepoImpl(get()) }
}
