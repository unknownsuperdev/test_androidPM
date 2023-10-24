package com.fiction.me.ui.fragments.mainlibrary.browsinghistory

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.core.ActionResult
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class BrowsingHistoryViewModel(
    private val getBrowsingHistoryBooksUseCase: FlowPagingUseCase<Unit, BookInfoAdapterModel>,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase
) : BaseViewModel() {

    private val _afterDeleteCurrentReadBook = MutableSharedFlow<Unit?>()
    val afterDeleteCurrentReadBook = _afterDeleteCurrentReadBook.asSharedFlow()

    private val _addBookToLib = MutableSharedFlow<Unit>()
    val addBookToLib = _addBookToLib.asSharedFlow()

    private val _removeBookFromLib = MutableSharedFlow<Unit>()
    val removeBookFromLib = _removeBookFromLib.asSharedFlow()
    var message = ""

    val pagingData = MutableStateFlow(1)
        .map { getBrowsingHistoryBooksUseCase(Unit) }
        .flatMapLatest { it }
        .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty()).cachedIn(viewModelScope)

    fun addOrRemoveBook(books: List<BookInfoAdapterModel>, bookId: Long, isAddedToLib: Boolean) {
        viewModelScope.launch {
            when (if (isAddedToLib) addBookToLibraryUseCase(bookId) else removeBookFromLibraryUseCase(
                bookId
            )) {
                is ActionResult.Success -> {
                    updateBookInfoList(books, bookId, isAddedToLib)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private fun updateBookInfoList(books: List<BookInfoAdapterModel>, id: Long, isSaved: Boolean) {
        books.map { if (it.id == id) it.isSaved = isSaved }
        viewModelScope.launch {
            if (isSaved)
                _addBookToLib.emit(Unit)
            else _removeBookFromLib.emit(Unit)
        }
    }

/*    fun deleteBookFromLib(bookId: Long) {
        val currentReadBooks = historyBookInfoList.value
        val findItem = currentReadBooks?.find { it.id == bookId }
        if (findItem != null) {
            val newList = currentReadBooks.toMutableList().apply {
                remove(findItem)
            }
            viewModelScope.launch {
                delay(100)
                _afterDeleteCurrentReadBook.emit(Unit)
                _historyBookInfoList.emit(newList)
            }
        }
    }*/
}