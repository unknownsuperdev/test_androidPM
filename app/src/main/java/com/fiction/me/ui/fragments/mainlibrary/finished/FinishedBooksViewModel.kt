package com.fiction.me.ui.fragments.mainlibrary.finished

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.GetFinishedReadBooksUseCase
import com.fiction.domain.interactors.GetLibAlsoLikeBooksUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BooksDataModel
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FinishedBooksViewModel(
    private val getLibAlsoLikeBooksUseCase: GetLibAlsoLikeBooksUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase,
    private val getFinishedReadBooksUseCase: GetFinishedReadBooksUseCase
) : BaseViewModel() {

    private val _suggestedBooksList = MutableStateFlow<List<BooksDataModel>?>(null)
    val suggestedBooksList = _suggestedBooksList.asStateFlow()

    private val _finishedBooks = MutableStateFlow<List<BooksWithReadProgressBookData>?>(null)
    val finishedBooks = _finishedBooks.asStateFlow()

    private val _addedOrRemoveBook = MutableStateFlow<Boolean?>(null)
    val addedOrRemoveBook = _addedOrRemoveBook.asStateFlow()

    fun getFinishedBooks() {
        viewModelScope.launch {
            val cacheData = getFinishedReadBooksUseCase.getCacheData()
            _finishedBooks.emit(cacheData)
            when (val result = getFinishedReadBooksUseCase(true)) {
                is ActionResult.Success -> {
                    _finishedBooks.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun getSuggestedBooksList() {
        viewModelScope.launch {
            val cacheData = getLibAlsoLikeBooksUseCase.getCacheData()
            _suggestedBooksList.emit(cacheData)
            when (val result = getLibAlsoLikeBooksUseCase(true)) {
                is ActionResult.Success -> {
                    _suggestedBooksList.emit(result.result)
                }
                is ActionResult.Error -> {

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
                    _suggestedBooksList.emit(updatedList)
                    _addedOrRemoveBook.emit(isAddedLibrary)
                }
                is ActionResult.Error -> {

                }
            }
        }
    }
}