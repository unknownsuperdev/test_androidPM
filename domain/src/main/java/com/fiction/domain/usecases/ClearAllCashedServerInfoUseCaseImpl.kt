package com.fiction.domain.usecases

import com.fiction.core.dispatcher.CoroutineDispatcherProvider
import com.fiction.domain.interactors.ClearAllCashedServerInfoUseCase
import com.fiction.domain.interactors.ExploreDataUseCase
import com.fiction.domain.interactors.GetAvailableTariffsUseCase
import com.fiction.domain.interactors.GetBookChaptersUseCase
import com.fiction.domain.interactors.GetBooksForYouUseCase
import com.fiction.domain.interactors.GetCurrentReadingBooksUseCase
import com.fiction.domain.interactors.GetFinishedReadBooksUseCase
import com.fiction.domain.interactors.GetLibAlsoLikeBooksUseCase
import com.fiction.domain.interactors.GetLibraryBooksUseCase
import com.fiction.domain.interactors.GetMostPopularBooksUseCase
import com.fiction.domain.interactors.GetPopularTagsUseCase
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.interactors.GetSuggestionBooksUseCase
import kotlinx.coroutines.withContext

class ClearAllCashedServerInfoUseCaseImpl(
    private val getLibraryBooksUseCase: GetLibraryBooksUseCase,
    private val exploreDataUseCase: ExploreDataUseCase,
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val getBooksForYouUseCase: GetBooksForYouUseCase,
    private val getCurrentReadingBooksUseCase: GetCurrentReadingBooksUseCase,
    private val getLibAlsoLikeBooksUseCase: GetLibAlsoLikeBooksUseCase,
    private val getFinishedReadBooksUseCase: GetFinishedReadBooksUseCase,
    private val getAvailableTariffsUseCase: GetAvailableTariffsUseCase,
    private val getMostPopularBooksUseCase: GetMostPopularBooksUseCase,
    private val getBookChaptersUseCase: GetBookChaptersUseCase,
    private val getSuggestionBooksUseCase: GetSuggestionBooksUseCase,
    private val dispatcher: CoroutineDispatcherProvider,
): ClearAllCashedServerInfoUseCase {

    override suspend fun invoke() {
        withContext(dispatcher.io) {
            getLibraryBooksUseCase(isClear = true)
            exploreDataUseCase(isClear = true)
            getPopularTagsUseCase(isClear = true)
            //getProfileInfoUseCase(isClear = true)
            getBooksForYouUseCase(isClear = true)
            //getCurrentReadingBooksUseCase(isClear = true)
            getLibAlsoLikeBooksUseCase(isClear = true)
            getFinishedReadBooksUseCase(isClear = true)
            getAvailableTariffsUseCase(isClearList = true)
            getMostPopularBooksUseCase(isClear = true)
            getBookChaptersUseCase(currentBookId = -1L,isClear = true)
            getSuggestionBooksUseCase(-1L, isClearList = true)
        }
    }
}