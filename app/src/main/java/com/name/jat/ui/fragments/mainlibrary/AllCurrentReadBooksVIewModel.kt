package com.name.jat.ui.fragments.mainlibrary

import androidx.lifecycle.viewModelScope
import com.name.domain.model.AllCurrentReadBooksDataModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllCurrentReadBooksVIewModel : BaseViewModel() {

    private val _allCurrentReadsBookList =
        MutableStateFlow<List<AllCurrentReadBooksDataModel>?>(null)
    val allCurrentReadsBookList = _allCurrentReadsBookList.asStateFlow()

    fun getAllCurrentReadsBooksList() {
        val bookList = listOf(
            AllCurrentReadBooksDataModel(
                20,
                "Book1",
                24,
                "Written as an homage to Homer’s epic poem The Odyssey, Ulysses follows its hero, Leopold Blofdfdfdfvdgdgvdgvgv"
            ),
            AllCurrentReadBooksDataModel(
                21,
                "Book2",
                45,
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf"
            ),
            AllCurrentReadBooksDataModel(
                1241,
                "Book3",
                99,
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf",
            ),
            AllCurrentReadBooksDataModel(
                22,
                "Book4",
                45,
                "husdhgfvuhdfvdfvdcvdcjdhbfvdfvhdfhvd sjhfcsjcfhjsh dhcvfdh dhfidjf"
            )
        )
        viewModelScope.launch {
            _allCurrentReadsBookList.emit(bookList)
        }
    }
}
