package com.name.jat.ui.fragments.mainlibrary.browsinghistory

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BookInfoAdapterModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BrowsingHistorySearchViewModel : BaseViewModel() {
    private val _searchResult: MutableStateFlow<List<BookInfoAdapterModel>?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val searchResult = _searchResult.asStateFlow()

    fun getSearchResult(word: String) {
        val bookList = listOf(
            BookInfoAdapterModel(
                20,
                "Book1",
                imageLink = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                "Written as an homage to Homer’s epic poem The Odyssey, Ulysses follows its hero, Leopold Blofdfdfdfvdgdgvdgvgv",
                23,
                45
            ),
            BookInfoAdapterModel(
                21,
                "Book2",
                imageLink = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf",
                23,
                45
            ),
            BookInfoAdapterModel(
                1241,
                "Book3",
                imageLink = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf",
                23,
                45
            ),
            BookInfoAdapterModel(
                22,
                "Book4",
                imageLink = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf",
                23,
                45
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

    fun updateBookInfoListAdapter(id: Long, isSaved: Boolean) {
        val findItem = searchResult.value?.find { it.id == id }
        val index = if (findItem != null) searchResult.value?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = searchResult.value?.toMutableList().apply {
                this?.set(index, this[index].copy(isSaved = isSaved))
            }

            viewModelScope.launch {
                _searchResult.emit(updatedList)
            }
        }
    }
}