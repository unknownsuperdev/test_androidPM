package com.name.jat.ui.fragments.mainlibrary.finished

import androidx.lifecycle.viewModelScope
import com.name.domain.model.AllCurrentReadBooksDataModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SeeAllFinishedBooksViewModel : BaseViewModel() {

    private val _allFinishedBookList = MutableStateFlow<List<AllCurrentReadBooksDataModel>?>(null)
    val allFinishedBookList = _allFinishedBookList.asStateFlow()

    fun getAllFinishedBookList() {
        val bookList = listOf(
            AllCurrentReadBooksDataModel(
                20,
                "Book1",
                100,
                "Written as an homage to Homer’s epic poem The Odyssey, Ulysses follows its hero, Leopold Blofdfdfdfvdgdgvdgvgv"
            ),
            AllCurrentReadBooksDataModel(
                21,
                "Book2",
                100,
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf"
            ),
            AllCurrentReadBooksDataModel(
                1241,
                "Book3",
                100,
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf",
            ),
            AllCurrentReadBooksDataModel(
                22,
                "Book4",
                100,
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf"
            )
        )
        viewModelScope.launch {
            _allFinishedBookList.emit(bookList)
        }
    }
}
