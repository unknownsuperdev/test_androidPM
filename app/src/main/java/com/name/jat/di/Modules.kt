package com.name.jat.di

import com.name.core.dispatcher.BaseCoroutineDispatcherProvider
import com.name.core.dispatcher.CoroutineDispatcherProvider
import com.name.domain.interactors.GuestUseCase
import com.name.domain.usecases.GuestUseCaseImpl
import com.name.jat.ui.MainViewModel
import com.name.jat.ui.fragments.booksummmary.BookSummaryViewModel
import com.name.jat.ui.fragments.explore.ExploreViewModel
import com.name.jat.ui.fragments.explore.SuggestBookSeeAllViewModel
import com.name.jat.ui.fragments.explore.geners.AllGenresViewModel
import com.name.jat.ui.fragments.explore.geners.BooksByGenresViewModel
import com.name.jat.ui.fragments.mainlibrary.AllCurrentReadBooksVIewModel
import com.name.jat.ui.fragments.mainlibrary.LibrarySearchViewModel
import com.name.jat.ui.fragments.mainlibrary.MainLibraryViewModel
import com.name.jat.ui.fragments.mainlibrary.addedtolibrary.AddedToLibraryViewModel
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuActionViewModel
import com.name.jat.ui.fragments.mainlibrary.bottomdialog.ContextMenuHistoryViewModel
import com.name.jat.ui.fragments.mainlibrary.browsinghistory.BrowsingHistorySearchViewModel
import com.name.jat.ui.fragments.mainlibrary.browsinghistory.BrowsingHistoryViewModel
import com.name.jat.ui.fragments.mainlibrary.downloaded.DownloadedBooksSearchViewModel
import com.name.jat.ui.fragments.mainlibrary.downloaded.DownloadedBooksViewModel
import com.name.jat.ui.fragments.mainlibrary.finished.FinishedBooksViewModel
import com.name.jat.ui.fragments.mainlibrary.finished.SeeAllFinishedBooksViewModel
import com.name.jat.ui.fragments.search.SearchMainViewModel
import com.name.jat.ui.fragments.search.defaultpage.SearchDefaultPageViewModel
import com.name.jat.ui.fragments.search.mainsearch.SearchViewModel
import com.name.jat.ui.reader.ReaderViewModel
import com.name.jat.ui.reader.ToolsViewModel
import com.name.jat.ui.fragments.explore.filterbytag.FilterByPopularTagsViewModel
import com.name.jat.ui.fragments.explore.filterbytag.FilteredBooksViewModel
import com.name.jat.ui.fragments.profile.ProfileViewModel
import com.name.jat.ui.fragments.profile.editprofile.EditProfileViewModel
import com.name.jat.ui.fragments.profile.purchasehistory.PurchaseHistoryViewModel
import com.name.jat.ui.fragments.profile.settings.AutoUnlockViewModel
import com.name.jat.ui.fragments.profile.settings.ReadingModeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { MainViewModel() }
    viewModel { ExploreViewModel(get(), get(), get(), get()) }
    viewModel { BookSummaryViewModel(get(), get()) }
    viewModel { SearchDefaultPageViewModel() }
    viewModel { SearchMainViewModel() }
    viewModel { MainLibraryViewModel() }
    viewModel { AllCurrentReadBooksVIewModel() }
    viewModel { LibrarySearchViewModel() }
    viewModel { FinishedBooksViewModel() }
    viewModel { SeeAllFinishedBooksViewModel() }
    viewModel { BrowsingHistoryViewModel() }
    viewModel { BrowsingHistorySearchViewModel() }
    viewModel { ContextMenuHistoryViewModel() }
    viewModel { ContextMenuActionViewModel() }
    viewModel { DownloadedBooksViewModel() }
    viewModel { DownloadedBooksSearchViewModel() }
    viewModel { AddedToLibraryViewModel(get()) }
    viewModel { ReaderViewModel(get(), get()) }
    viewModel { ToolsViewModel(get(), get()) }
    viewModel { SearchViewModel(get(), get(), get()) }
    viewModel { AllGenresViewModel() }
    viewModel { BooksByGenresViewModel() }
    viewModel { FilterByPopularTagsViewModel() }
    viewModel { FilteredBooksViewModel() }
    viewModel { ProfileViewModel(get(),get()) }
    viewModel { PurchaseHistoryViewModel() }
    viewModel { EditProfileViewModel(get(),get(),get()) }
    viewModel { ReadingModeViewModel(get(),get()) }
    viewModel { AutoUnlockViewModel(get(),get()) }
    viewModel { SuggestBookSeeAllViewModel() }
}

val dispatcherModule = module {
    single<CoroutineDispatcherProvider> { BaseCoroutineDispatcherProvider() }
    single<GuestUseCase> { GuestUseCaseImpl(get(), get(), get()) }
}
