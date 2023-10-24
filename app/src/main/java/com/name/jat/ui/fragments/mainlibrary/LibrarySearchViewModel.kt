package com.name.jat.ui.fragments.mainlibrary

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BooksWithReadProgressBookData
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LibrarySearchViewModel : BaseViewModel() {
    private val _searchResult = MutableStateFlow<List<BooksWithReadProgressBookData>?>(null)
    val searchResult = _searchResult.asStateFlow()

    fun getSearchResult(word: String) {
        val bookList = listOf(
            BooksWithReadProgressBookData(
                90,
                "Day and night",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                45
            ),
            BooksWithReadProgressBookData(
                91,
                "Wife after love",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                23

            ),
            BooksWithReadProgressBookData(
                92,
                "One night with you",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                4
            ),
            BooksWithReadProgressBookData(
                93,
                "Day and night",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                75
            )
        )
        viewModelScope.launch {
            _searchResult.emit(bookList)
        }
    }

    fun clearSearchResult() {
        viewModelScope.launch {
            _searchResult.emit(null)
        }
    }
}