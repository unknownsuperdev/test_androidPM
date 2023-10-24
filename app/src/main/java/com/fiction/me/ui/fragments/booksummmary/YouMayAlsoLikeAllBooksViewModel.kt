package com.fiction.me.ui.fragments.booksummmary

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.GetAllSuggestionBooksUseCase
import com.fiction.domain.interactors.GetLibAllAlsoLikeBooksUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.enums.FeedTypes
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events.Companion.ADDED_TO_LIBRARY
import com.fiction.me.utils.Events.Companion.BOOK_CLICKED
import com.fiction.me.utils.Events.Companion.BOOK_ID_PROPERTY
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SCREEN
import com.fiction.me.utils.Events.Companion.BOOK_SUMMARY_SHOWN
import com.fiction.me.utils.Events.Companion.FINISHED
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.YOU_MAY_ALSO_LIKE
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class YouMayAlsoLikeAllBooksViewModel(
    //private val getAllSuggestionBooksUseCase: GetAllSuggestionBooksUseCase,
    private val getLibAllAlsoLikeBooksUseCase: GetLibAllAlsoLikeBooksUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
) : BaseViewModel() {

    private val _bookInfoList = MutableStateFlow<List<BookInfoAdapterModel>?>(null)
    val bookInfoList = _bookInfoList.asStateFlow()

    private val _printMessageAddedLib = MutableSharedFlow<Unit?>()
    val printMessageAddedLib = _printMessageAddedLib.asSharedFlow()

    private val _printMessageRemoveLib = MutableSharedFlow<Unit?>()
    val printMessageRemoveLib = _printMessageRemoveLib.asSharedFlow()

    fun getBooks() {
        viewModelScope.launch {
            when (val result = getLibAllAlsoLikeBooksUseCase()) {
                is ActionResult.Success -> {
                    _bookInfoList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateBookItemData(
        id: Long,
        isSaved: Boolean
    ) {
        val updatedList = bookInfoList.value?.toMutableList()
        if (!updatedList.isNullOrEmpty()) {
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
        val findItem = books.find { it.id == id }
        val index = if (findItem != null) books.indexOf(findItem) else -1
        if (index != -1) {
            val updatedList = books.toMutableList().apply {
                this[index] = this[index].copy(isSaved = isSaved)
            }
            viewModelScope.launch {
                if (isSaved)
                    _printMessageAddedLib.emit(Unit)
                else _printMessageRemoveLib.emit(Unit)
                _bookInfoList.emit(updatedList)
            }
        }
    }

    fun sendEvent(feedType: FeedTypes, id: Long) {
        val placement = when (feedType) {
            FeedTypes.YOU_MAY_ALSO_LIKE_FROM_BOOK_SUMMARY -> BOOK_SUMMARY_SCREEN
            FeedTypes.YOU_MAY_ALSO_LIKE_FROM_FINISHED -> FINISHED
            else -> ADDED_TO_LIBRARY
        }
        trackEvents(
            BOOK_CLICKED,
            hashMapOf(BOOK_ID_PROPERTY to id, PLACEMENT to placement)
        )
        trackEvents(
            BOOK_SUMMARY_SHOWN,
            hashMapOf(PLACEMENT to placement, SECTION to YOU_MAY_ALSO_LIKE, BOOK_ID_PROPERTY to id)
        )
    }
}