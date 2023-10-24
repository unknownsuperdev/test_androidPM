package com.name.jat.ui.fragments.search.mainsearch

import androidx.lifecycle.viewModelScope
import com.name.core.ActionResult
import com.name.domain.interactors.CleanSearchHistoryUseCase
import com.name.domain.interactors.SearchBookUseCase
import com.name.domain.interactors.SearchHistoryUseCase
import com.name.domain.model.SearchBookWithName
import com.name.domain.model.SearchBookWithTag
import com.name.domain.model.SearchMainItem
import com.name.jat.appbase.viewmodel.BaseViewModel
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

    private fun createItem(word: String) {
        val singleSearchItem = mutableListOf<SearchMainItem>()
        singleSearchItem.add(SearchBookWithTag(0, word))
        _searchResultsList.value = singleSearchItem
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
                    TODO()
                }
            }
        }
    }

    fun getHistoryList() {
        viewModelScope.launch {
            /* when (val result = searchHistoryUseCase()) {
                 is ActionResult.Success -> {
                     _searchResultsList.emit(result.result)
                 }
                 is ActionResult.Error -> {
                     TODO()// Error handling
                 }
                 }*/

            val list = listOf(
                SearchBookWithName(45, "History", "Gender"),
                SearchBookWithName(67, "History", "Gender"),
                SearchBookWithTag(13, "History"),
                SearchBookWithTag(35, "History")
            )

            _recentInfo.value = true
            _searchResultsList.emit(list)
        }
    }
}
