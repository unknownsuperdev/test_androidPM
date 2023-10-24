package com.name.jat.ui.fragments.mainlibrary.browsinghistory

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BookInfoAdapterModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BrowsingHistoryViewModel : BaseViewModel() {
    private val _historyBookInfoList = MutableStateFlow<List<BookInfoAdapterModel>?>(null)
    val historyBookInfoList = _historyBookInfoList.asStateFlow()

    private val _afterDeleteCurrentReadBook = MutableSharedFlow<Unit?>()
    val afterDeleteCurrentReadBook = _afterDeleteCurrentReadBook.asSharedFlow()

    var message = ""

    fun getHistoryBooksList() {
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
            _historyBookInfoList.emit(bookList)
        }
    }

    fun updateBookInfoList(id: Long, isSaved: Boolean) {
        val findItem = historyBookInfoList.value?.find { it.id == id }
        val index = if (findItem != null) historyBookInfoList.value?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = historyBookInfoList.value?.toMutableList().apply {
                this?.set(index, this[index].copy(isSaved = isSaved))
            }

            viewModelScope.launch {
                _historyBookInfoList.emit(updatedList)
            }
        }
    }

    fun deleteBookFromLib(bookId: Long) {
        val currentReadBooks = historyBookInfoList.value
        val findItem = currentReadBooks?.find { it.id == bookId }
        if (findItem != null) {
            val newList = currentReadBooks.toMutableList().apply {
                remove(findItem)
            }
            viewModelScope.launch {
                delay(100)
                _afterDeleteCurrentReadBook.emit(Unit)
                _historyBookInfoList.emit(newList)
            }
        }
    }
}