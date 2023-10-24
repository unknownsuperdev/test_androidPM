package com.name.jat.ui.fragments.search.defaultpage

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BookInfoAdapterModel
import com.name.domain.model.PopularTagsDataModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchDefaultPageViewModel : BaseViewModel() {

    private val _bookInfoList = MutableStateFlow<List<BookInfoAdapterModel>?>(null)
    val bookInfoList = _bookInfoList.asStateFlow()

    private val _popularTagsList = MutableStateFlow<List<PopularTagsDataModel>?>(null)
    val popularTagsList = _popularTagsList.asStateFlow()

    fun getSuggestedBooksList() {
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
            _bookInfoList.emit(bookList)
        }
    }

    fun getPopularTagsList() {
        val list = listOf(
            PopularTagsDataModel(20, "vdsvg", false),
            PopularTagsDataModel(21, "Family", false),
            PopularTagsDataModel(1241, "Fantasy", false),
            PopularTagsDataModel(22, "Alpha", false)
        )
        viewModelScope.launch {
            _popularTagsList.emit(list)
        }
    }

    fun updateBookInfoListAdapter(id: Long, isSaved: Boolean) {
        val findItem = bookInfoList.value?.find { it.id == id }
        val index = if (findItem != null) bookInfoList.value?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = bookInfoList.value?.toMutableList().apply {
                this?.set(index, this[index].copy(isSaved = isSaved))
            }

            viewModelScope.launch {
                _bookInfoList.emit(updatedList)
            }
        }
    }
}