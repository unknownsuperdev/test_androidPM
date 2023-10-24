package com.fiction.me.ui.fragments.explore

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.core.ActionResult
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.SuggestedBookParams
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.EXPLORE
import com.fiction.me.utils.Events.Companion.FOR_YOU
import com.fiction.me.utils.Events.Companion.HOT_ROMANCE
import com.fiction.me.utils.Events.Companion.HOT_WEREWOLF
import com.fiction.me.utils.Events.Companion.NEW_ARRIVAL
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.POPULAR
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.TOP_WEEKLY_BESTSELLERS
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SuggestBookSeeAllViewModel(
    private val getFeedAllBooksUseCase: FlowPagingUseCase<SuggestedBookParams, BookInfoAdapterModel>,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase
) : BaseViewModel() {

    private val _printMessageAddedLib = MutableSharedFlow<Unit?>()
    val printMessageAddedLib = _printMessageAddedLib.asSharedFlow()

    private val _printMessageRemoveLib = MutableSharedFlow<Unit?>()
    val printMessageRemoveLib = _printMessageRemoveLib.asSharedFlow()

    fun getAllBooksList(feedType: FeedTypes, id: Int) =
        MutableStateFlow(1)
            .map { getFeedAllBooksUseCase(SuggestedBookParams(feedType, id)) }
            .flatMapLatest { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
            .cachedIn(viewModelScope)

    fun getTitle(feedType: FeedTypes) = when (feedType) {
        FeedTypes.FOR_YOU -> "For You"
        FeedTypes.POPULAR -> "Popular"
        FeedTypes.MOST_POPULAR -> "Most Popular Stories"
        FeedTypes.HOT_ROMANCE -> "Hot Romance"
        FeedTypes.TOP_WEEKLY_BESTSELLERS -> "Top Weekly Bestsellers"
        FeedTypes.HOT_WEREWOLF -> "Hot Werewolf"
        FeedTypes.NEW_ARRIVAL -> "New Arrivals"
        FeedTypes.YOU_MAY_ALSO_LIKE_FROM_LIBRARY -> "You May Also Like"
        FeedTypes.YOU_MAY_ALSO_LIKE_FROM_BOOK_SUMMARY -> "You May Also Like"
        else -> "Sale" // FeedTypes.SALE
    }

    fun updateBookItemData(
        updatedList: List<BookInfoAdapterModel>,
        id: Long,
        isSaved: Boolean
    ) {
        if (updatedList.isNotEmpty()) {
            viewModelScope.launch {
                if (isSaved) {
                    when (addBookToLibraryUseCase(id)) {
                        is ActionResult.Success -> {
                            updateBookInfoListAdapter(
                                id,
                                isSaved,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                } else {
                    when (removeBookFromLibraryUseCase(id)) {
                        is ActionResult.Success -> {
                            updateBookInfoListAdapter(
                                id,
                                isSaved,
                                updatedList
                            )
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                }
            }
        }
    }

    private fun updateBookInfoListAdapter(
        id: Long,
        isSaved: Boolean,
        books: List<BookInfoAdapterModel>
    ) {
        books.map { if (it.id == id) it.isSaved = isSaved }
        viewModelScope.launch {
            if (isSaved)
                _printMessageAddedLib.emit(Unit)
            else _printMessageRemoveLib.emit(Unit)
        }
    }

    fun sendEvent(feedType: FeedTypes, id: Long) {
        val section = when (feedType) {
            FeedTypes.FOR_YOU -> FOR_YOU
            FeedTypes.POPULAR -> POPULAR
            FeedTypes.MOST_POPULAR -> POPULAR
            FeedTypes.HOT_ROMANCE -> HOT_ROMANCE
            FeedTypes.TOP_WEEKLY_BESTSELLERS -> TOP_WEEKLY_BESTSELLERS
            FeedTypes.HOT_WEREWOLF -> HOT_WEREWOLF
            else -> NEW_ARRIVAL
        }
        trackEvents(
            BOOK_SUMMARY_SHOWN,
            hashMapOf(PLACEMENT to EXPLORE, SECTION to section, BOOK_ID_PROPERTY to id)
        )
    }
}
