package com.name.jat.ui.fragments.explore.geners

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BookInfoAdapterModel
import com.name.domain.model.BookInfoAdapterModelList
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BooksByGenresViewModel : BaseViewModel() {
    private val _bookInfoList: MutableStateFlow<BookInfoAdapterModelList?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val bookInfoList = _bookInfoList.asStateFlow()

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
                    45,
                    sale = 50
                )
            )
        )
        viewModelScope.launch {
            _bookInfoList.emit(bookList)
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
}