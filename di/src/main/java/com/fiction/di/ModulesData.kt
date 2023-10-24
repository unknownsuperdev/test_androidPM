package com.fiction.di

import android.app.Application
import androidx.room.Room
import com.core.preference.provider.ProviderPreferenceStore
import com.core.preference.provider.ProviderPreferenceStoreImpl
import com.fiction.core.utils.Uuid
import com.localebro.okhttpprofiler.OkHttpProfilerInterceptor
import com.fiction.data.BuildConfig
import com.fiction.data.dataservice.apiservice.*
import com.fiction.data.dataservice.appservice.PreferencesService
import com.fiction.data.dataservice.appservice.PreferencesServiceImpl
import com.fiction.data.dataservice.sqlservice.AppDatabase
import com.fiction.data.dataservice.sqlservice.Migration
import com.fiction.data.repository.*
import com.fiction.data.util.HeaderInterceptor
import com.fiction.domain.repository.*
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

val apiModule = module {
    single<Retrofit> {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .apply {
                val okHttpClientBuilder = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(HeaderInterceptor(get()))
                if (BuildConfig.DEBUG) {
                    okHttpClientBuilder
                        .addInterceptor(OkHttpProfilerInterceptor())
                }
                client(
                    okHttpClientBuilder
                        .build()
                )
            }
            .build()
    }

    single<Retrofit>(named("Auth")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .apply {
                val okHttpClientBuilder = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(HeaderInterceptor(get()))

                if (BuildConfig.DEBUG) {
                    okHttpClientBuilder
                        .addInterceptor(OkHttpProfilerInterceptor())
                }
                client(
                    okHttpClientBuilder
                        .build()
                )
            }
            .build()
    }

    single<Retrofit>(named("Token")) {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL_AUTH)
            .addConverterFactory(GsonConverterFactory.create())
            .apply {
                val okHttpClientBuilder = OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)

                if (BuildConfig.DEBUG) {
                    okHttpClientBuilder
                        .addInterceptor(OkHttpProfilerInterceptor())
                }
                client(
                    okHttpClientBuilder
                        .build()
                )
            }
            .build()
    }

    single<AllApiService> { get<Retrofit>().create(AllApiService::class.java) }
    single<AuthService> { get<Retrofit>(named("Auth")).create(AuthService::class.java) }
    single<TokenService> { get<Retrofit>(named("Token")).create(TokenService::class.java) }
    single<ExploreService> { get<Retrofit>().create(ExploreService::class.java) }
    single<LibraryService> { get<Retrofit>().create(LibraryService::class.java) }
    single<BookStateService> { get<Retrofit>().create(BookStateService::class.java) }
    single<SearchService> { get<Retrofit>().create(SearchService::class.java) }
    single<BookSummaryService> { get<Retrofit>().create(BookSummaryService::class.java) }
    single<ProfileService> { get<Retrofit>().create(ProfileService::class.java) }
    single<OnBoardingService> { get<Retrofit>().create(OnBoardingService::class.java) }
    single<ReaderApiService> { get<Retrofit>().create(ReaderApiService::class.java) }
    single<FinishedReadBooksService> { get<Retrofit>().create(FinishedReadBooksService::class.java) }
    single<CoinShopService> { get<Retrofit>().create(CoinShopService::class.java) }
    single<TagsService> { get<Retrofit>().create(TagsService::class.java) }
    single<GiftsService> { get<Retrofit>().create(GiftsService::class.java) }
    single<AnalyticService> { get<Retrofit>().create(AnalyticService::class.java) }
}
val databaseModule = module {
    fun provideDatabase(application: Application): AppDatabase {
        return Room.databaseBuilder(application, AppDatabase::class.java, "JatDB")
            .addMigrations(Migration.MIGRATION_2_3, Migration.MIGRATION_3_4)
            .allowMainThreadQueries()
            .build()
    }
    single { provideDatabase(androidApplication()) }
    single { get<AppDatabase>().settingsDao() }
    single { get<AppDatabase>().onBoardingSettingDao() }
    single { get<AppDatabase>().imagesDao() }
    single { get<AppDatabase>().bookChapterDao() }
    single { get<AppDatabase>().pushNotificationDao() }
    single { get<AppDatabase>().launchInfoDao() }
}
val repositoryModule = module {
    single<TestRepository> { TestRepositoryImpl(get()) }
    single<AuthRepository> { AuthRepositoryImpl(get()) }
    single<DataStoreRepository> { DataStoreRepositoryImpl(get()) }
    single<ExploreDataRepository> { ExploreDataRepositoryImpl(get(), get()) }
    single<SearchBookRepository> { SearchBookRepositoryImpl(get(), get()) }
    single<PreferencesService> { PreferencesServiceImpl(get()) }
    single<BookSettingsRepo> { BookSettingsRepoImpl(get()) }
    single<LibraryRepository> { LibraryRepositoryImpl(get(),get()) }
    single<BookStateRepo> { BookStateRepoImpl(get()) }
    single<BookSummeryRepo> { BookSummaryRepoImpl(get(), get()) }
    single<ProfileRepo> { ProfileRepoImpl(get()) }
    single<OnBoardingRepo> { OnBoardingRepoImpl(get()) }
    single<OnBoardingSettingRepo> { OnBoardingSettingRepoImpl(get()) }
    single<ReaderRepo> { ReaderRepoImpl(get()) }
    single<FinishedReadBooksRepo> { FinishedReadBooksRepositoryImpl(get()) }
    single<CoinShopRepo> { CoinShopRepositoryImpl(get()) }
    single<TagsRepo> { TagsRepoImpl(get(), get()) }
    single<ImagesRepo> { ImagesRepoImpl(get()) }
    single<BookChapterRepo> { BookChapterRepoImpl(get()) }
    single<GiftRepository> { GiftRepositoryImpl(get()) }
    single<LibraryRepoWithoutPagingRepo> { LibraryRepoWithoutPagingRepoImpl(get()) }
    single<PushNotificationInfoRepo> { PushNotificationInfoRepoImpl(get()) }
    single<AnalyticRepo> { AnalyticRepoImpl(get()) }
    single<LaunchInfoRepo> { LaunchInfoRepoImpl(get()) }
    single<TokenRepository> { TokenRepositoryImpl(get()) }
    single<CacheImageRepo> { CacheImageRepoImpl(get()) }
    single<AppInfoRepository> { AppInfoRepositoryImpl(get(named(Constant.Qualifier.Preference.APP_INFO))) }
}

val preferenceModule = module {
    factory(named(Constant.Qualifier.Preference.APP_INFO)) {
        (get() as ProviderPreferenceStore).providePreferenceStore(
            get(),
            Constant.File.ANALYTICS,
            false
        )
    }

    single<ProviderPreferenceStore> {
        ProviderPreferenceStoreImpl()
    }
    single<TokenRepository> { TokenRepositoryImpl(get()) }
}

val coreModule = module {
    single { Uuid(get()) }
}

object Constant {
    object Qualifier {
        object Preference {
            const val APP_INFO = "app_info"
        }
    }
    object File {
        const val ANALYTICS = "analytics-preference"
    }
}
