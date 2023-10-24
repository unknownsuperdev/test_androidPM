package com.fiction.me.ui.fragments.mainlibrary.browsinghistory

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fiction.core.ActionResult
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.interactors.SetViewInBookUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BrowsingHistorySearchViewModel(
    private val getSearchedHistoryBooksUseCase: FlowPagingUseCase<String, BookInfoAdapterModel>,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
    private val setViewInBookUseCase: SetViewInBookUseCase
) : BaseViewModel() {

    private val _addBookToLib = MutableSharedFlow<Unit>()
    val addBookToLib = _addBookToLib.asSharedFlow()

    private val _removeBookFromLib = MutableSharedFlow<Unit>()
    val removeBookFromLib = _removeBookFromLib.asSharedFlow()

    private val _searchWord = MutableStateFlow<String?>(null)

    val pagingData = _searchWord
        .map { getSearchedHistoryBooksUseCase(it ?: "") }
        .flatMapLatest { it }
        .cachedIn(viewModelScope)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun getSearchResult(word: String) {
        viewModelScope.launch {
            _searchWord.emit(word)
        }
    }

    fun clearSearchResult() {
        viewModelScope.launch {
            _searchWord.emit(null)
        }
    }

    fun addOrRemoveBook(books: List<BookInfoAdapterModel>, bookId: Long, isAddedToLib: Boolean) {
        viewModelScope.launch {
            when (if (isAddedToLib) addBookToLibraryUseCase(bookId) else removeBookFromLibraryUseCase(
                bookId
            )) {
                is ActionResult.Success -> {
                    updateBookInfoListAdapter(books, bookId, isAddedToLib)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun setViewInBook(bookId: Long) {
        viewModelScope.launch {
            when (setViewInBookUseCase(bookId)) {
                is ActionResult.Success -> {
                    Log.i("setViewInBook", "Success")
                }
                is ActionResult.Error -> {

                }
            }
        }
    }

    private fun updateBookInfoListAdapter(
        books: List<BookInfoAdapterModel>,
        id: Long,
        isAddedToLib: Boolean
    ) {
        books.map { if (it.id == id) it.isSaved = isAddedToLib }
        viewModelScope.launch {
            if (isAddedToLib)
                _addBookToLib.emit(Unit)
            else _removeBookFromLib.emit(Unit)
        }
    }
}
