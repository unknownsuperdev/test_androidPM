package com.fiction.me.ui.fragments.mainlibrary.finished

import android.util.Log
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fiction.core.ActionResult
import com.fiction.domain.baseusecase.FlowPagingUseCase
import com.fiction.domain.interactors.SetViewInBookUseCase
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FinishedBooksSearchViewModel(
    private val getSearchedFinishBooksUseCase: FlowPagingUseCase<String, BooksWithReadProgressBookData>,
    private val setViewInBookUseCase: SetViewInBookUseCase
) : BaseViewModel() {

    private val _searchWord = MutableStateFlow<String?>(null)

    val pagingData = _searchWord
        .map { getSearchedFinishBooksUseCase(it ?: "") }
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
}