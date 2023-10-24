package com.fiction.me.ui.fragments.explore.geners

import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*

class BooksByGenresViewModel(
    private val getBooksWithGenreByIdUseCase: FlowPagingUseCase<Long, BookInfoAdapterModel>,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase
) : BaseViewModel() {

    private val _addBookToLib = MutableSharedFlow<Long>()
    val addBookToLib = _addBookToLib.asSharedFlow()

    private val _removeBookFromLib = MutableSharedFlow<Long>()
    val removeBookFromLib = _removeBookFromLib.asSharedFlow()

    fun getGenreBooksList(genreId: Long) =
        MutableStateFlow(1)
            .map {  getBooksWithGenreByIdUseCase(genreId) }
            .flatMapLatest { it }
            .stateIn(viewModelScope, SharingStarted.Lazily, PagingData.empty())
            .cachedIn(viewModelScope)

/*    fun addOrRemoveBook(bookId: Long, isAddedToLib: Boolean) {
        viewModelScope.launch {
            when (if (isAddedToLib) addBookToLibraryUseCase(bookId) else removeBookFromLibraryUseCase(
                bookId
            )) {
                is ActionResult.Success -> {
                    updateBookInfoListAdapter(bookId, isAddedToLib)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }*/

/*    private fun updateBookInfoListAdapter(id: Long, isSaved: Boolean) {
        val updatedList = bookInfoList.value?.books?.map {
            if (it.id == id) it.copy(isSaved = isSaved) else it
        }
        val newBookInfoItem = updatedList?.let { bookInfoList.value?.copy(books = it) }
        viewModelScope.launch {
            if (isSaved)
                _addBookToLib.emit(id)
            else _removeBookFromLib.emit(id)
            _bookInfoList.emit(newBookInfoItem)
        }
    }*/
}