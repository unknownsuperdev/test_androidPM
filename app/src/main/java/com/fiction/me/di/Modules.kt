package com.fiction.me.di

import android.app.Application
import android.app.NotificationManager
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import com.amplitude.api.Amplitude
import com.amplitude.api.AmplitudeClient
import com.appsflyer.AppsFlyerLib
import com.fiction.core.utils.Constants.Companion.ALL_BOOKS_LIBRARY
import com.fiction.core.utils.Constants.Companion.ALL_FEED_BOOKS
import com.fiction.core.utils.Constants.Companion.BROWSING_HISTORY_BOOKS
import com.fiction.core.utils.Constants.Companion.FILTERED_TAGS_BOOKS
import com.fiction.core.utils.Constants.Companion.ALL_FINISHED_READ_BOOKS
import com.fiction.core.utils.Constants.Companion.BOOKS_BY_GENRE
import com.fiction.core.utils.Constants.Companion.SEARCH_BROWSING_HISTORY_BOOKS
import com.fiction.core.utils.Constants.Companion.SEARCH_FINISHED_BOOKS
import com.fiction.core.utils.Constants.Companion.SEARCH_LIBRARY_BOOKS
import com.fiction.core.utils.Constants.Companion.YOU_MAY_ALSO_LIKE
import com.fiction.core.utils.Uuid
import com.fiction.me.BuildConfig
import com.fiction.me.notification.Notifications
import com.fiction.me.notification.NotificationImpl
import com.fiction.me.notification.PushParser
import com.fiction.me.ui.MainViewModel
import com.fiction.me.ui.analytics.Analytics
import com.fiction.me.ui.analytics.AnalyticsImpl
import com.fiction.me.ui.fragments.booksummmary.BookSummaryViewModel
import com.fiction.me.ui.fragments.booksummmary.YouMayAlsoLikeAllBooksViewModel
import com.fiction.me.ui.fragments.booksummmary.YouMayAlsoLikeBooksWithPagingViewModel
import com.fiction.me.ui.fragments.chapterlist.ChapterViewModel
import com.fiction.me.ui.fragments.explore.ExploreViewModel
import com.fiction.me.ui.fragments.explore.GetMoreCoinsViewModel
import com.fiction.me.ui.fragments.explore.SuggestBookSeeAllViewModel
import com.fiction.me.ui.fragments.explore.filterbytag.BooksByTagViewModel
import com.fiction.me.ui.fragments.explore.filterbytag.FilterByPopularTagsViewModel
import com.fiction.me.ui.fragments.explore.filterbytag.FilteredBooksViewModel
import com.fiction.me.ui.fragments.explore.geners.AllGenresViewModel
import com.fiction.me.ui.fragments.explore.geners.BooksByGenresViewModel
import com.fiction.me.ui.fragments.explore.notenoughcoins.NotEnoughCoinsViewModel
import com.fiction.me.ui.fragments.gift.WelcomeGiftViewModel
import com.fiction.me.ui.fragments.loginregistration.SignInOrSignUpViewModel
import com.fiction.me.ui.fragments.loginregistration.registrationproviders.*
import com.fiction.me.ui.fragments.mainlibrary.AllCurrentReadBooksViewModel
import com.fiction.me.ui.fragments.mainlibrary.LibrarySearchViewModel
import com.fiction.me.ui.fragments.mainlibrary.MainLibraryViewModel
import com.fiction.me.ui.fragments.mainlibrary.addedtolibrary.AddedToLibraryBooksSearchViewModel
import com.fiction.me.ui.fragments.mainlibrary.addedtolibrary.AddedToLibraryViewModel
import com.fiction.me.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionViewModel
import com.fiction.me.ui.fragments.mainlibrary.bottomdialog.ContextMenuHistoryViewModel
import com.fiction.me.ui.fragments.mainlibrary.browsinghistory.BrowsingHistorySearchViewModel
import com.fiction.me.ui.fragments.mainlibrary.browsinghistory.BrowsingHistoryViewModel
import com.fiction.me.ui.fragments.mainlibrary.finished.FinishedBooksSearchViewModel
import com.fiction.me.ui.fragments.mainlibrary.finished.FinishedBooksViewModel
import com.fiction.me.ui.fragments.mainlibrary.finished.SeeAllFinishedBooksViewModel
import com.fiction.me.ui.fragments.onboarding.*
import com.fiction.me.ui.fragments.profile.ProfileViewModel
import com.fiction.me.ui.fragments.profile.purchasehistory.PurchaseHistoryViewModel
import com.fiction.me.ui.fragments.profile.settings.AutoUnlockViewModel
import com.fiction.me.ui.fragments.profile.settings.DataStorageViewModel
import com.fiction.me.ui.fragments.profile.settings.ReadingModeViewModel
import com.fiction.me.ui.fragments.profile.settings.SettingsViewModel
import com.fiction.me.ui.fragments.purchase.*
import com.fiction.me.ui.fragments.purchase.maindialog.MainDialogViewModel
import com.fiction.me.ui.fragments.reader.OtherReportViewModel
import com.fiction.me.ui.fragments.reader.ReaderViewModel
import com.fiction.me.ui.fragments.reader.ReportViewModel
import com.fiction.me.ui.fragments.reader.ToolsViewModel
import com.fiction.me.ui.fragments.retention.RetentionInReaderViewModel
import com.fiction.me.ui.fragments.search.SearchMainViewModel
import com.fiction.me.ui.fragments.search.defaultpage.SearchDefaultPageViewModel
import com.fiction.me.ui.fragments.search.mainsearch.SearchViewModel
import com.fiction.me.ui.fragments.search.mainsearch.questiondialog.QuestionDialogViewModel
import com.fiction.me.utils.*
import com.fiction.me.ui.fragments.dataProvider.AdIdDataSource
import com.fiction.me.ui.fragments.dataProvider.AdIdDataSourceImpl
import com.fiction.me.ui.fragments.dataProvider.AppsFlyerImpl
import com.fiction.me.ui.fragments.dataProvider.IAppsFlyer
import com.fiction.me.ui.fragments.loginregistration.SignUpWithEmailViewModel
import com.fiction.me.ui.fragments.loginregistration.signin.SignInWithMailViewModel
import com.fiction.me.ui.fragments.loginregistration.verification.VerifyEmailAddressViewModel
import com.google.firebase.auth.FirebaseAuth
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ExploreViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { BookSummaryViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(),get(named(YOU_MAY_ALSO_LIKE))) }
    viewModel { SearchDefaultPageViewModel(get(), get(), get(), get()) }
    viewModel { SearchMainViewModel(get()) }
    viewModel { MainLibraryViewModel(get(), get(), get(), get()) }
    viewModel { AllCurrentReadBooksViewModel(get(named(ALL_BOOKS_LIBRARY))) }
    viewModel { LibrarySearchViewModel(get(named(SEARCH_LIBRARY_BOOKS)), get()) }
    viewModel { FinishedBooksViewModel(get(), get(), get(), get()) }
    viewModel { SeeAllFinishedBooksViewModel(get(named(ALL_FINISHED_READ_BOOKS))) }
    viewModel { BrowsingHistoryViewModel(get(named(BROWSING_HISTORY_BOOKS)),get(), get()) }
    viewModel { BrowsingHistorySearchViewModel(get(named(SEARCH_BROWSING_HISTORY_BOOKS)),get(),get(), get()) }
    viewModel { ContextMenuHistoryViewModel() }
    viewModel { ContextMenuActionViewModel() }
    viewModel { AddedToLibraryViewModel(get(), get(), get(),get()) }
    viewModel { ReaderViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ToolsViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { AllGenresViewModel(get()) }
    viewModel { BooksByGenresViewModel(get(named(BOOKS_BY_GENRE)), get(), get()) }
    viewModel { FilterByPopularTagsViewModel(get()) }
    viewModel { FilteredBooksViewModel(get(named(FILTERED_TAGS_BOOKS)), get(), get()) }
    viewModel { ProfileViewModel(get(), get(), get()) }
    viewModel { PurchaseHistoryViewModel() }
    viewModel { ReadingModeViewModel(get(), get()) }
    viewModel { AutoUnlockViewModel(get(), get()) }
    viewModel { SuggestBookSeeAllViewModel(get(named(ALL_FEED_BOOKS)), get(), get()) }
    viewModel { SpecialOfferGetCoinViewModel() }
    viewModel { CoinShopViewModel(get(), get(), get(), get()) }
    viewModel { SplashViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { ChooseFavoriteThemeViewModel(get(), get(), get(), get(), get()) }
    viewModel { SelectionBooksViewModel(get()) }
    viewModel { RetentionInReaderViewModel(get()) }
    viewModel { SelectGenderViewModel(get(), get(), get(), get()) }
    viewModel { PickReadingTimeViewModel(get(), get(), get(), get()) }
    viewModel { ChapterViewModel(get()) }
    viewModel { BooksByTagViewModel(get()) }
    viewModel { FinishedBooksSearchViewModel(get(named(SEARCH_FINISHED_BOOKS)), get()) }
    viewModel { AddedToLibraryBooksSearchViewModel(get(named(SEARCH_LIBRARY_BOOKS)), get()) }
    viewModel { ReportViewModel(get()) }
    viewModel { OtherReportViewModel(get()) }
    viewModel { GetCoinForNotPayerUserViewModel() }
    viewModel { GetMoreCoinsViewModel() }
    viewModel { UnlockNewEpisodeViewModel(get(), get()) }
    viewModel { NotEnoughCoinsViewModel(get()) }
    viewModel { MainDialogViewModel() }
    viewModel { QuestionDialogViewModel() }
    viewModel { YouMayAlsoLikeAllBooksViewModel(get(), get(), get()) }
    viewModel { AutoUnlockStateViewModel(get()) }
    viewModel { WelcomeViewModel(get()) }
    viewModel { DataStorageViewModel() }
    viewModel { SettingsViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { WelcomeGiftViewModel(get()) }
    viewModel { SignInOrSignUpViewModel(get(), get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { AnalyzeNovelViewModel(get(), get()) }
    viewModel { YouMayAlsoLikeBooksWithPagingViewModel(get(named(YOU_MAY_ALSO_LIKE))) }
    viewModel { WriteToSupportViewModel() }
    viewModel { VerifyEmailAddressViewModel(get()) }
    viewModel { SignUpWithEmailViewModel(get(), get(), get(), get(), get(), get())}
    viewModel { SignInWithMailViewModel(get(), get(), get(),get(), get(), get()) }
}

val billingModule = module {
    single { Billing(get()) }
}

val amplitudeModule = module {
    fun provideAmplitudeInstance(application: Application, uuid: Uuid): AmplitudeClient {
        val amplitudeKey = BuildConfig.AMPLITUDE_KEY

        val client = Amplitude.getInstance()
            .initialize(application, amplitudeKey)
            .enableForegroundTracking(application)
        client.deviceId = uuid.getUuid()
        client.enableLogging(true)
        return client
    }
    single { provideAmplitudeInstance(androidApplication(), get()) }
    single<Analytics> { AnalyticsImpl(get(),get(),get(), get()) }
}

val appsFlyerModule = module {
    fun provideAppsFlyer(uuId: Uuid): AppsFlyerLib {
        return AppsFlyerLib.getInstance().apply {
            setCustomerUserId(uuId.getUuid())
        }
    }
    single { provideAppsFlyer(get()) }
    single { MyAppsFlyerConversionListener() }
    single<IAppsFlyer> { AppsFlyerImpl(get(), get(), androidContext()) }
}

val notificationModule = module {
    fun provideNotificationManager(context: Context): NotificationManager {
        return getSystemService(context, NotificationManager::class.java) as NotificationManager
    }
    single<Notifications> { NotificationImpl(get(), get()) }
    single { provideNotificationManager(get()) }
    single { PushParser() }
}

val authModule = module {
    single { FirebaseAuth.getInstance() }
    single  <GoogleSignInProvider> { GoogleSignInWrapper(get()) }
    factory <FacebookSignInProvider> { FacebookSignInWrapper(get()) }
    factory <AppleSignInProvider> { AppleSignInWrapper(get()) }
}

val advertisingModule = module {
    single<AdIdDataSource> { AdIdDataSourceImpl(androidContext()) }
}