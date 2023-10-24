package com.fiction.me.ui.fragments.mainlibrary.addedtolibrary

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.GetLibAlsoLikeBooksUseCase
import com.fiction.domain.interactors.GetLibraryBooksUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BooksDataModel
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddedToLibraryViewModel(
    private val getBooksLibraryUseCase: GetLibraryBooksUseCase,
    private val getLibAlsoLikeBooksUseCase: GetLibAlsoLikeBooksUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
) : BaseViewModel() {

    private val _suggestedBooksList = MutableStateFlow<List<BooksDataModel>?>(null)
    val suggestedBooksList = _suggestedBooksList.asStateFlow()

    private val _addedOrRemoveBook = MutableSharedFlow<Boolean?>()
    val addedOrRemoveBook = _addedOrRemoveBook.asSharedFlow()

    private val _libraryBooks = MutableStateFlow<List<BooksWithReadProgressBookData>?>(null)
    val libraryBooks = _libraryBooks.asStateFlow()
    var isOpened = false

    fun getLibraryBooks() {
        viewModelScope.launch {
            when (val result = getBooksLibraryUseCase(isOpened)) {
                is ActionResult.Success -> {
                    isOpened = true
                    _libraryBooks.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun getSuggestedBooksList(makeCall: Boolean = false) {
        viewModelScope.launch {
            if (!makeCall) {
                val cacheData = getLibAlsoLikeBooksUseCase.getCacheData()
                _suggestedBooksList.emit(cacheData)
            }
            when (val result = getLibAlsoLikeBooksUseCase(true)) {
                is ActionResult.Success -> {
                    _suggestedBooksList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateSuggestedBooksList(dataModelId: Long, isAddedLibrary: Boolean) {
        val updatedList =
            suggestedBooksList.value?.map {
                if (dataModelId == it.id)
                    it.copy(isAddedLibrary = isAddedLibrary)
                else it
            }
        viewModelScope.launch {
            val result =
                if (isAddedLibrary) addBookToLibraryUseCase(dataModelId) else removeBookFromLibraryUseCase(
                    dataModelId
                )
            when (result) {
                is ActionResult.Success -> {
                    getSuggestedBooksList(true)
                    _addedOrRemoveBook.emit(isAddedLibrary)
                    delay(800)
                    getLibraryBooks()
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }
}