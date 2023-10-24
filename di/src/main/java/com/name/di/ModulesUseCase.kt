package com.name.di

import com.name.domain.interactors.*
import com.name.domain.usecases.*
import org.koin.dsl.module

val useCaseModule = module {
    factory<TestUseCase> { TestUseCaseImpl(get(), get()) }
    factory<GuestUseCase> { GuestUseCaseImpl(get(), get(), get()) }
    factory<GetBooksLibraryUseCase> { GetBooksLibraryUseCaseImpl(get(), get()) }
    factory<AddBookToLibraryUseCase> { AddBookToLibraryUseCaseImpl(get(), get()) }
    factory<RemoveBookFromLibraryUseCase> { RemoveBookFromLibraryUseCaseImpl(get(), get()) }
    factory<CleanSearchHistoryUseCase> { CleanSearchHistoryUseCaseImpl(get(), get()) }
    factory<ExploreDataUseCase> { ExploreDataUseCaseImpl(get(), get()) }
    factory<SearchBookUseCase> { SearchBookUseCaseImpl(get(), get()) }
    factory<SearchHistoryUseCase> { SearchHistoryUseCaseImpl(get(), get()) }
    factory<BookSettingsGetUseCase> { BookSettingsGetUseCaseImpl(get(), get()) }
    factory<BookSettingsInsertUseCase> { BookSettingsInsertUseCaseImpl(get()) }
    factory<TextFontsUseCase> { TextFontsUseCaseImpl() }
    factory<SetIsOpenedWelcomeScreenUseCase> { SetIsOpenedWelcomeScreenUseCaseImpl(get()) }
    factory<GetIsOpenedWelcomeScreenUseCase> { GetIsOpenedWelcomeScreenUseCaseImpl(get()) }
    factory<GetUserTokenFromDatastoreUseCase> { GetUserTokenFromDatastoreUseCaseImpl(get()) }
    factory<GetProfileInfoUseCase> { GetProfileInfoUseCaseImpl(get(), get()) }
    factory<RemoveProfileAvatarUseCase> { RemoveProfileAvatarUseCaseImpl(get(), get()) }
    factory<UpdateProfileUseCase> { UpdateProfileUseCaseImpl(get(), get()) }
}
