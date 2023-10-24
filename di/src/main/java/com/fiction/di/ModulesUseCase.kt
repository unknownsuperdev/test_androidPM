package com.fiction.di

import com.fiction.core.dispatcher.BaseCoroutineDispatcherProvider
import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.core.utils.Constants.Companion.ALL_BOOKS_LIBRARY
import com.fiction.core.utils.Constants.Companion.ALL_CURRENT_READING_BOOKS
import com.fiction.core.utils.Constants.Companion.ALL_FEED_BOOKS
import com.fiction.core.utils.Constants.Companion.BROWSING_HISTORY_BOOKS
import com.fiction.core.utils.Constants.Companion.FILTERED_TAGS_BOOKS
import com.fiction.core.utils.Constants.Companion.ALL_FINISHED_READ_BOOKS
import com.fiction.core.utils.Constants.Companion.BOOKS_BY_GENRE
import com.fiction.core.utils.Constants.Companion.SEARCH_BROWSING_HISTORY_BOOKS
import com.fiction.core.utils.Constants.Companion.SEARCH_FINISHED_BOOKS
import com.fiction.core.utils.Constants.Companion.SEARCH_LIBRARY_BOOKS
import com.fiction.core.utils.Constants.Companion.YOU_MAY_ALSO_LIKE
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.*
import com.fiction.domain.interactors.GetSuggestionBooksUseCase
import com.fiction.domain.model.*
import com.fiction.domain.usecases.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val useCaseModule = module {
    factory<TestUseCase> { TestUseCaseImpl(get(), get()) }
    factory<GuestUseCase> { GuestUseCaseImpl(get(), get(), get(), get()) }
    single<GetLibraryBooksUseCase> { GetLibraryBooksUseCaseImpl(get(), get(), get(), get()) }
    factory<AddBookToLibraryUseCase> { AddBookToLibraryUseCaseImpl(get(), get(), get(), get(), get()) }
    factory<RemoveBookFromLibraryUseCase> { RemoveBookFromLibraryUseCaseImpl(get(), get(), get(), get(), get()) }
    factory<CleanSearchHistoryUseCase> { CleanSearchHistoryUseCaseImpl(get(), get()) }
    single<ExploreDataUseCase> { ExploreDataUseCaseImpl(get(), get(), get(), get(), get()) }
    factory<SearchBookUseCase> { SearchBookUseCaseImpl(get(), get(), get(), get()) }
    factory<SearchHistoryUseCase> { SearchHistoryUseCaseImpl(get(), get()) }
    factory<BookSettingsGetUseCase> { BookSettingsGetUseCaseImpl(get(), get()) }
    factory<BookSettingsInsertUseCase> { BookSettingsInsertUseCaseImpl(get()) }
    factory<TextFontsUseCase> { TextFontsUseCaseImpl() }
    single<GetAllGenresUseCase> { GetAllGenresUseCaseImpl(get(), get(), get(), get()) }
    factory<GetBookInfoByIdUseCase> { GetBookInfoByIdUseCaseImpl(get(), get(), get(), get()) }
    factory<GetChapterInfoUseCase> { GetChapterInfoUseCaseImpl(get(), get(), get(), get(), get()) }
    factory<SetReadProgressUseCase> { SetReadProgressUseCaseImpl(get(), get()) }
    factory<GetAllTagsUseCase> { GetAllTagsUseCaseImpl(get(), get()) }
    single<GetPopularTagsUseCase> { GetPopularTagsUseCaseImpl(get(), get()) }
    factory<GetTagBooksByIdUseCase> { GetTagBooksByIdUseCaseImpl(get(), get(), get(), get()) }
    factory<GetUserTokenFromDatastoreUseCase> { GetUserTokenFromDatastoreUseCaseImpl(get()) }
    single<GetProfileInfoUseCase> { GetProfileInfoUseCaseImpl(get(), get(), get()) }
    factory<UpdateProfileUseCase> { UpdateProfileUseCaseImpl(get(), get(), get()) }
    factory<GetGenderUseCase> { GetGenderUseCaseImpl(get(), get()) }
    factory<GetFavoriteReadingTimeUseCase> { GetFavoriteReadingTimeUseCaseImpl(get(), get()) }
    factory<GetFavoriteTagsUseCase> { GetFavoriteTagsUseCaseImpl(get(), get(), get()) }
    single <GetBooksForYouUseCase> { GetBooksForYouUseCaseImpl(get(), get(), get(), get(), get(), get()) }
    factory<UpdateOnBoardingSettingUseCase> { UpdateOnBoardingSettingUseCaseImpl(get(), get()) }
    factory<GetOnBoardingScreensUseCase> { GetOnBoardingScreensUseCaseImpl(get(), get()) }
    factory<GetOnBoardingSettingUseCase> { GetOnBoardingSettingUseCaseImpl(get()) }
    factory<InsertOnBoardingSettingUseCase> { InsertOnBoardingSettingUseCaseImpl(get()) }
    factory<DeleteOnBoardingSettingUseCase> { DeleteOnBoardingSettingUseCaseImpl(get()) }
    factory<FlowPagingUseCase<SuggestedBookParams, BookInfoAdapterModel>>(named(ALL_FEED_BOOKS)) { GetFeedAllBooksUseCaseImpl(get(), get()) }
    factory<SetViewInBookUseCase> { SetViewInBookUseCaseImpl(get(), get()) }
    factory<SendReportUseCase> { SendReportUseCaseImpl(get(), get()) }
    factory<SendReaderAnalyticsUseCase>  { SendReaderAnalyticsUseCaseImpl(get(), get())  }
    factory<SetLikeUseCase> { SetLikeUseCaseImpl(get(), get(), get(), get()) }
    factory<RemoveLikeUseCase> { RemoveLikeUseCaseImpl(get(), get(), get(), get()) }
    factory<FlowPagingUseCase<Unit, BookInfoAdapterModel>>(named(BROWSING_HISTORY_BOOKS)) { GetBrowsingHistoryBooksUseCaseImpl(get(), get()) }
    single <GetCurrentReadingBooksUseCase>{ GetCurrentReadingBooksUseCaseImpl(get(), get(), get(), get()) }
    single<GetLibAlsoLikeBooksUseCase> { GetLibAlsoLikeBooksUseCaseImpl(get(), get(), get(), get()) }
   // factory<GetAllSuggestionBooksUseCase> { GetAllSuggestionBooksUseCaseImpl(get(), get(), get(), get()) }
    factory<FlowPagingUseCase<Long, BookInfoAdapterModel>>(named(YOU_MAY_ALSO_LIKE)) { GetAllYouMayAlsoLikeBooksUseCaseImpl(get(), get()) }
    factory<FlowPagingUseCase<Long, BookInfoAdapterModel>>(named(BOOKS_BY_GENRE)) { GetBooksWithGenreByIdUseCaseImpl(get(), get()) }
    factory<GetLibAllAlsoLikeBooksUseCase> { GetLibAllAlsoLikeBooksUseCaseImpl(get(), get(), get(), get()) }
    factory<FlowPagingUseCase<Unit, AllCurrentReadBooksDataModel>>(named(ALL_CURRENT_READING_BOOKS)) { GetAllCurrentReadingBooksUseCaseImpl(get(), get()) }
    factory<FlowPagingUseCase<OpenedAllBooks, AllCurrentReadBooksDataModel>>(named(ALL_BOOKS_LIBRARY)) { GetAllBooksLibraryUseCaseImpl(get(), get()) }
    single <GetFinishedReadBooksUseCase> { GetFinishedReadBooksUseCaseImpl(get(), get(), get(), get()) }
    factory<FlowPagingUseCase<Unit, AllCurrentReadBooksDataModel>>(named(ALL_FINISHED_READ_BOOKS)) { GetAllFinishedReadBooksUseCaseImpl(get(), get()) }
    factory<GetBalanceUseCase> { GetBalanceUseCaseImpl(get(), get(), get()) }
    factory<BuyTariffUseCase> { BuyTariffUseCaseImpl(get(), get(), get()) }
    factory<BuyChapterUseCase> { BuyChapterUseCaseImpl(get(), get(), get(), get()) }
    single<GetAvailableTariffsUseCase> { GetAvailableTariffsUseCaseImpl(get(), get()) }
    factory<FlowPagingUseCase<List<Long>, BookInfoAdapterModel>> (named(FILTERED_TAGS_BOOKS)){ GetFilteredTagsBooksUseCaseImpl(get(), get()) }
    factory<FlowPagingUseCase<String, BooksWithReadProgressBookData>>(named(SEARCH_LIBRARY_BOOKS))  { GetSearchedBooksLibraryUseCaseImpl(get(), get()) }
    factory<FlowPagingUseCase<String, BookInfoAdapterModel>> (named(SEARCH_BROWSING_HISTORY_BOOKS)) { GetSearchedHistoryBooksUseCaseImpl(get(), get()) }
    factory<FlowPagingUseCase<String, BooksWithReadProgressBookData>> (named(SEARCH_FINISHED_BOOKS)) { GetSearchedFinishBooksUseCaseImpl(get(), get()) }
    factory<GetSearchedMainLibBooksUseCase> { GetSearchedMainLibBooksUseCaseImpl(get(), get(), get(), get()) }
    factory<GetChapterFromDBUseCase> { GetChapterFromDBUseCaseImp(get()) }
    factory<InsertChapterUseCase> { InsertChapterUseCaseImpl(get()) }
    factory<InsertBookUseCase> { InsertBookUseCaseImpl(get()) }
    factory<GetImageFromCachingUseCase> { GetImageFromCachingUseCaseImpl(get(), get()) }
    factory<CachingBookImagesUseCase> { CachingBookImagesUseCaseImpl(get(), get()) }
    factory<GetIsAppLaunchFirstTimeUseCase> { GetIsAppLaunchFirstTimeUseCaseImpl(get()) }
    factory<SetIsAppLaunchFirstTimeUseCase> { SetIsAppLaunchFirstTimeUseCaseImpl(get()) }
    factory<SetIsDataRestoredUseCase> { SetIsDataRestoredUseCaseImpl(get()) }
    factory<GetIsDataRestoredUseCase> { GetIsDataRestoredUseCaseImpl(get()) }
    factory<GetIsExistGiftUseCase> { GetIsExistGiftUseCaseImpl(get(), get()) }
    factory<GetGiftUseCase> { GetGiftUseCaseImpl(get(), get()) }
    factory<CompleteGiftUseCase> { CompleteGiftUseCaseImpl(get(), get(), get()) }
    single<GetMostPopularBooksUseCase> { GetMostPopularBooksUseCaseImpl(get(), get(), get(), get()) }
    single<GetBookChaptersUseCase> { GetBookChaptersUseCaseImpl(get(), get()) }
    factory<UpdateDeviceUseCase> { UpdateDeviceUseCaseImpl(get(), get()) }
    factory<SetPushTokenUseCase> { SetPushTokenUseCaseImpl(get(), get(), get()) }
    factory<SavePushTokenLocalUseCase> { SavePushTokenLocalUseCaseImpl(get()) }
    factory<GetPushTokenFromLocalUseCase> { GetPushTokenFromLocalUseCaseImpl(get()) }
    factory<GetPushNotIdFromLocalDbUseCase> { GetPushNotIdFromLocalDbUseCaseImpl(get()) }
    factory<RegisterTokenUseCase> { RegisterTokenUseCaseImpl(get(), get(), get(), get()) }
    factory<LoginTokenUseCase> { LoginTokenUseCaseImpl(get(), get(), get(), get()) }
    factory<LogoutUseCase> { LogoutUseCaseImpl(get(), get(), get()) }
    factory<LoginWithFBUseCase> { LoginWithFacebookImpl(get(), get(), get()) }
    factory<LoginWithGoogleUseCase> { LoginWithGoogleImpl(get(), get(), get()) }
    factory<LoginWithAppleUseCase> { LoginWithAppleImpl(get(), get(), get()) }
    factory<SetIsGetGuestNewTokenStateUseCase> { SetIsGetGuestNewTokenStateUseCaseImpl(get()) }
    factory<StoreUuidUseCase> { StoreUuidUseCaseImpl(get()) }
    factory<GetUuidUseCase> { GetUuidUseCaseImpl(get()) }
    factory<SetPushNotIdToLocalDbUseCase> { SetPushNotIdToLocalDbUseCaseImpl(get()) }
    factory<SetAppsflyerDataUseCase> { SetAppsflyerDataUseCaseImpl(get(), get()) }
    factory<SetAppleDataUseCase> { SetAppleDataUseCaseImpl(get(), get()) }
    factory<SetAdjustDataUseCase> { SetAdjustDataUseCaseImpl(get(), get()) }
    factory<SetFacebookDataUseCase> { SetFacebookDataUseCaseImpl(get(), get()) }
    factory<SetGoogleDataUseCase> { SetGoogleDataUseCaseImpl(get(), get(), get()) }
    factory<SetTiktokDataUseCase> { SetTiktokDataUseCaseImpl(get(), get()) }
    factory<GetIsLoggedStatusUseCase> { GetIsLoggedStatusUseCaseImpl(get()) }
    single <GetSuggestionBooksUseCase> { GetSuggestionBooksUseCaseImpl(get(), get(), get(), get(), get()) }
    factory<GetAlsoLikeBooksUseCase> { GetAlsoLikeBooksUseCaseImpl(get(), get(), get(), get(), get()) }
    factory<GetLaunchInfoFromDBUseCase> { GetLaunchInfoFromDBUseCaseImpl(get()) }
    factory<InsertLaunchInfoFromDBUseCase> { InsertLaunchInfoFromDBUseCaseImpl(get()) }
    factory<ForgotPasswordUseCase> { ForgotPasswordUseCaseImpl(get(), get()) }
    factory<CheckResetCodeUseCase> { CheckResetCodeUseCaseImpl(get(), get()) }
    factory<ResetPasswordUseCase> { ResetPasswordUseCaseImpl(get(), get()) }
    factory<SetAndroidDataUseCase> { SetAndroidDataUseCaseImpl(get(), get(), get()) }
    factory<ResendEmailUseCase> { ResendEmailUseCaseImpl(get(), get(), get()) }
    factory<GetAuthTypeUseCase> { GetAuthTypeUseCaseImpl(get()) }
    factory<SetAuthTypeUseCase> { SetAuthTypeUseCaseImpl(get()) }
    factory<ClearAllCashedServerInfoUseCase> {
        ClearAllCashedServerInfoUseCaseImpl(get(), get(), get(),get(), get(), get(),get(), get(), get(),get(), get(), get(),get())
    }
    factory<SetImageCacheUseCase> { SetImageCacheUseCaseImpl(get(), get()) }
    factory<GetIsExploreFirstTimeUseCase> { GetIsExploreFirstTimeUseCaseImpl(get()) }
    factory<SetIsExploreFirstTimeUseCase> { SetIsExploreFirstTimeUseCaseImpl(get()) }
}

val dispatcherModule = module {
    single<CoroutineDispatcherProvider> { BaseCoroutineDispatcherProvider() }
}
