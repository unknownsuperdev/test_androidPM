package com.name.jat.ui.fragments.booksummmary

import androidx.lifecycle.viewModelScope
import com.name.core.ActionResult
import com.name.domain.interactors.AddBookToLibraryUseCase
import com.name.domain.interactors.RemoveBookFromLibraryUseCase
import com.name.domain.model.BooksDataModel
import com.name.domain.model.PopularTagsDataModel
import com.name.domain.model.PopularTagsModel
import com.name.domain.model.SuggestedBooksModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BookSummaryViewModel(
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase
) : BaseViewModel() {
    private val _suggestedBooksList = MutableStateFlow<SuggestedBooksModel?>(null)
    val suggestedBooksList = _suggestedBooksList.asStateFlow()

    private val _popularTagsList = MutableStateFlow<PopularTagsModel?>(null)
    val popularTagsList = _popularTagsList.asStateFlow()

    private val _readBook = MutableSharedFlow<Unit>()
    val readBook = _readBook.asSharedFlow()

    private val _addlike = MutableSharedFlow<Unit>()
    val addlike = _addlike.asSharedFlow()

    private val _removeLike = MutableSharedFlow<Unit>()
    val removeLike = _removeLike.asSharedFlow()

    private val _addBookToLib = MutableSharedFlow<Unit>()
    val addBookToLib = _addBookToLib.asSharedFlow()

    private val _removeBookFromLib = MutableSharedFlow<Unit>()
    val removeBookFromLib = _removeBookFromLib.asSharedFlow()

    var likeBook = false
    var isAddedBookInLib = false

    fun getSuggestedBooksList() {
        val suggestedBooksModel = SuggestedBooksModel(
            "222", "You May Also Like", listOf(
                BooksDataModel(46, "", "Fantasy", "Romance", false, 50),
                BooksDataModel(47, "", "Fantasy", "Romance", false, 50),
                BooksDataModel(13, "", "Fantasy", "Romance", false, 50),
                BooksDataModel(41, "", "Fantasy", "Romance", false, 50),
                BooksDataModel(20, "", "Fantasy", "Romance", false, 50)
            )
        )

        viewModelScope.launch {
            _suggestedBooksList.emit(suggestedBooksModel)
        }
    }

    fun getPopularTagsList() {
        val popularTagsModel = PopularTagsModel(
            "555", "Popular Tags", listOf(
                PopularTagsDataModel(20, "vdsvg"),
                PopularTagsDataModel(21, "Family"), PopularTagsDataModel(1241, "Fantasy"),
                PopularTagsDataModel(22, "Alpha")
            )
        )
        viewModelScope.launch {
            _popularTagsList.emit(popularTagsModel)
        }
    }

    fun updateSuggestedBooksList(bookId: Long, isAddedLibrary: Boolean) {
        viewModelScope.launch {
            if (isAddedLibrary) {
                when (addBookToLibraryUseCase(bookId)) {
                    is ActionResult.Success -> {
                        updateLibraryBooksList(
                            bookId,
                            isAddedLibrary
                        )
                    }
                    is ActionResult.Error -> {
                        TODO() // Error handling
                    }
                }
            } else {
                when (removeBookFromLibraryUseCase(bookId)) {
                    is ActionResult.Success -> {
                        updateLibraryBooksList(
                            bookId,
                            isAddedLibrary
                        )
                    }
                    is ActionResult.Error -> {
                        TODO() // Error handling
                    }
                }
            }
        }
    }

    fun startReadBook() {
        viewModelScope.launch {
            delay(1000)
            _readBook.emit(Unit)
        }
    }

    fun changeBookLikeState() {
        viewModelScope.launch {
            delay(500)
            if (likeBook)
                _removeLike.emit(Unit)
            else _addlike.emit(Unit)
            likeBook = !likeBook
        }
    }

    fun addOrRemoveBook() {
        viewModelScope.launch {
            delay(500)
            if (isAddedBookInLib)
                _removeBookFromLib.emit(Unit)
            else _addBookToLib.emit(Unit)
            isAddedBookInLib = !isAddedBookInLib
        }
    }

    private fun updateLibraryBooksList(
        bookId: Long,
        isAddedLibrary: Boolean
    ) {
        var itemSuggestedBooks = suggestedBooksList.value
        val findItem = itemSuggestedBooks?.booksList?.find { it.id == bookId }
        val index = if (findItem != null) itemSuggestedBooks?.booksList?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = itemSuggestedBooks?.booksList?.toMutableList().apply {
                this?.set(index, this[index].copy(isAddedLibrary = isAddedLibrary))
            }
            itemSuggestedBooks = updatedList?.let { itemSuggestedBooks?.copy(booksList = it) }
        }

        viewModelScope.launch {
            _suggestedBooksList.emit(itemSuggestedBooks)
        }
    }
}