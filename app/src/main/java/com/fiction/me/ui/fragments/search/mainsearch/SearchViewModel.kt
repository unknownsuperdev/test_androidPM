package com.fiction.me.ui.fragments.search.mainsearch

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.CleanSearchHistoryUseCase
import com.fiction.domain.interactors.SearchBookUseCase
import com.fiction.domain.interactors.SearchHistoryUseCase
import com.fiction.domain.model.SearchBookWithTag
import com.fiction.domain.model.SearchMainItem
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val searchBookUseCase: SearchBookUseCase,
    private val cleanSearchHistoryUseCase: CleanSearchHistoryUseCase,
    private val searchHistoryUseCase: SearchHistoryUseCase,
) : BaseViewModel() {

    private val _searchResultsList = MutableStateFlow<List<SearchMainItem>?>(null)
    val searchResultsList = _searchResultsList.asStateFlow()

    private val _recentInfo = MutableStateFlow(false)
    val recentInfo = _recentInfo.asStateFlow()
    var isFoundResult = true

    fun getSearchResults(word: String?, searchByClick: Boolean) {

        word?.let {
            viewModelScope.launch {
                when (val result = searchBookUseCase(word)) {
                    is ActionResult.Success -> {

                        if (result.result.isNotEmpty()) {
                            _searchResultsList.value = result.result
                        } else {
                            if (searchByClick)
                                _searchResultsList.value = result.result
                            else {
                                createItem(word)
                            }
                        }
                        _recentInfo.value = false
                    }
                    is ActionResult.Error -> {
                        if (searchByClick) {
                            _searchResultsList.value = emptyList()
                        } else {
                            createItem(word)
                        }
                    }
                }
            }
        }
    }

    fun setSearchList(list: List<SearchMainItem>) {
        viewModelScope.launch {
            _searchResultsList.emit(list)
        }
    }

    fun clearSearchHistory() {
        viewModelScope.launch {
            when (cleanSearchHistoryUseCase()) {
                is ActionResult.Success -> {
                    _searchResultsList.emit(null)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun getHistoryList() {
        viewModelScope.launch {
            when (val result = searchHistoryUseCase()) {
                is ActionResult.Success -> {
                    if (result.result.isNotEmpty()) {
                        _recentInfo.value = true
                        _searchResultsList.emit(result.result)
                    }
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private fun createItem(word: String) {
        val singleSearchItem = mutableListOf<SearchMainItem>()
        singleSearchItem.add(SearchBookWithTag(0, word))
        isFoundResult = false
        _searchResultsList.value = singleSearchItem
    }

}
