package com.fiction.me.ui.fragments.search.defaultpage

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.AddBookToLibraryUseCase
import com.fiction.domain.interactors.GetMostPopularBooksUseCase
import com.fiction.domain.interactors.GetPopularTagsUseCase
import com.fiction.domain.interactors.RemoveBookFromLibraryUseCase
import com.fiction.domain.model.BookInfoAdapterModel
import com.fiction.domain.model.PopularTagsDataModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchDefaultPageViewModel(
    private val getPopularTagsUseCase: GetPopularTagsUseCase,
    private val getMostPopularBooksUseCase: GetMostPopularBooksUseCase,
    private val addBookToLibraryUseCase: AddBookToLibraryUseCase,
    private val removeBookFromLibraryUseCase: RemoveBookFromLibraryUseCase
) : BaseViewModel() {

    private val _updateBooks = MutableSharedFlow<Boolean>()
    val updateBooks = _updateBooks.asSharedFlow()

    private val _popularTagsList = MutableStateFlow<List<PopularTagsDataModel>?>(null)
    val popularTagsList = _popularTagsList.asStateFlow()

    private val _popularBooksList = MutableStateFlow<List<BookInfoAdapterModel>?>(null)
    val popularBooksList = _popularBooksList.asStateFlow()

    fun getMostPopularBooks(id: Long = -1L, isSaved: Boolean = false) {
        viewModelScope.launch {
            when (val result = getMostPopularBooksUseCase(id = id, isSaved = isSaved)) {
                is ActionResult.Success -> {
                    _popularBooksList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun getPopularTagsList() {
        viewModelScope.launch {
            when (val result = getPopularTagsUseCase()) {
                is ActionResult.Success -> {
                    _popularTagsList.emit(result.result)
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
        if (popularBooksList.value?.isNotEmpty() == true) {
            viewModelScope.launch {
                if (isSaved) {
                    when (addBookToLibraryUseCase(id)) {
                        is ActionResult.Success -> {
                            getMostPopularBooks(id, isSaved)
                            _updateBooks.emit(isSaved)
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                } else {
                    when (removeBookFromLibraryUseCase(id)) {
                        is ActionResult.Success -> {
                            getMostPopularBooks(id, isSaved)
                            _updateBooks.emit(isSaved)
                        }
                        is ActionResult.Error -> {
                            // TODO() //Error Handling
                        }
                    }
                }
            }
        }
    }
}