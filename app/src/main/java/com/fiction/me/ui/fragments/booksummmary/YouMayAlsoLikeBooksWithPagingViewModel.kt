package com.fiction.me.ui.fragments.booksummmary

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.core.ActionResult
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.GetAllSuggestionBooksUseCase
import com.fiction.domain.model.AllCurrentReadBooksDataModel
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.OpenedAllBooks
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SCREEN
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.YOU_MAY_ALSO_LIKE
import kotlinx.coroutines.flow.*


class YouMayAlsoLikeBooksWithPagingViewModel(
    private val getAllSuggestionBooksUseCase: FlowPagingUseCase<Long, BookInfoAdapterModel>
) : BaseViewModel() {

    fun getAllCurrentReadsBooksList(bookId: Long) =
        MutableStateFlow(1)
            .map { getAllSuggestionBooksUseCase(bookId) }
            .flatMapLatest { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
            .cachedIn(viewModelScope)


    fun sendEvent(id: Long) {
        trackEvents(
            BOOK_CLICKED,
            hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to BOOK_SUMMARY_SCREEN)
        )
        trackEvents(
            BOOK_SUMMARY_SHOWN,
            hashMapOf(
                PLACEMENT to BOOK_SUMMARY_SCREEN,
                SECTION to YOU_MAY_ALSO_LIKE,
                BOOK_ID_PROPERTY to id
            )
        )
    }
}