package com.name.jat.ui.fragments.explore.filterbytag

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BookInfoAdapterModel
import com.name.domain.model.BookInfoAdapterModelList
import com.name.domain.model.PopularTagsDataModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FilteredBooksViewModel : BaseViewModel() {

    private val _selectedPopularTagsList = MutableStateFlow<List<PopularTagsDataModel>?>(null)
    val selectedPopularTagsList = _selectedPopularTagsList.asStateFlow()

    private val _bookInfoList = MutableStateFlow<BookInfoAdapterModelList?>(null)
    val bookInfoList = _bookInfoList.asStateFlow()

    private val _emptyBookList = MutableSharedFlow<Unit?>()
    val emptyBookList = _emptyBookList.asSharedFlow()

    fun getSuggestedBooksList() {
        val bookList = BookInfoAdapterModelList(
            132, "Fantasy", listOf(
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
        )
        viewModelScope.launch {
            if (bookList.list.isEmpty()) {
                _emptyBookList.emit(Unit)
            } else _bookInfoList.emit(bookList)
        }
    }

    fun updateBookInfoListAdapter(id: Long, isSaved: Boolean) {
        val findItem = bookInfoList.value?.list?.find { it.id == id }
        val index = if (findItem != null) bookInfoList.value?.list?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = bookInfoList.value?.list?.toMutableList().apply {
                this?.set(index, this[index].copy(isSaved = isSaved))
            }
            val newBookInfoItem = updatedList?.let { bookInfoList.value?.copy(list = it) }
            viewModelScope.launch {
                _bookInfoList.emit(newBookInfoItem)
            }
        }
    }

    fun setSelectedTagsIdList(list: ArrayList<PopularTagsDataModel>) {
        viewModelScope.launch {
            _selectedPopularTagsList.emit(list.toList())
        }
    }

    fun updateSelectedItemsList(
        tagId: Long,
        isSelected: Boolean
    ) {
        var updatedList = selectedPopularTagsList.value?.toMutableList()

        val item = updatedList?.find { tagId == it.id }
        val index = updatedList?.indexOf(item)
        if (index != -1 && index != null) {
            updatedList = updatedList.apply {
                this?.get(index)?.copy(isSelected = isSelected)?.let { this[index] = it }
            }
        }
        viewModelScope.launch {
            _selectedPopularTagsList.emit(updatedList)
        }
    }

    fun getFilteredListById() = selectedPopularTagsList.value?.filter {
        it.isSelected
    }

}