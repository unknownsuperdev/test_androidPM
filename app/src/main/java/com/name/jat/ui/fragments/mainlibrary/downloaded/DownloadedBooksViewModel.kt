package com.name.jat.ui.fragments.mainlibrary.downloaded

import androidx.lifecycle.viewModelScope
import com.name.domain.model.BooksWithReadProgressBookData
import com.name.domain.model.NextInLineBooksData
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DownloadedBooksViewModel : BaseViewModel() {
    private val _currentReadBooks = MutableStateFlow<List<BooksWithReadProgressBookData>?>(null)
    val currentReadBooks = _currentReadBooks.asStateFlow()

    private val _nextInLineBooks = MutableStateFlow<List<NextInLineBooksData>?>(null)
    val nextInLineBooks = _nextInLineBooks.asStateFlow()

    private val _afterDeleteCurrentReadBook = MutableSharedFlow<Unit?>()
    val afterDeleteCurrentReadBook = _afterDeleteCurrentReadBook.asSharedFlow()

    var message = ""

    fun getCurrentReadBooksList() {
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
            _currentReadBooks.emit(bookList)
        }
    }

    fun getNextInLineBooksList() {
        val bookList = listOf(
            NextInLineBooksData(
                90,
                "Day and night",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            NextInLineBooksData(
                91,
                "Wife after love",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            NextInLineBooksData(
                92,
                "One night with you",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            ),
            NextInLineBooksData(
                93,
                "Day and night",
                cover = "https://media.istockphoto.com/photos/concept-image-of-a-magnifying-glass-on-blue-background-with-a-word-picture-id1316134499?s=612x612",
            )
        )
        viewModelScope.launch {
            _nextInLineBooks.emit(bookList)
        }
    }

    fun deleteBookFromDownloaded(bookId: Long) {
        val currentReadBooks = currentReadBooks.value
        val findItem = currentReadBooks?.find { it.id == bookId }
        if (findItem != null) {
            val newList = currentReadBooks.toMutableList().apply {
                remove(findItem)
            }
            viewModelScope.launch {
                delay(100)
                _currentReadBooks.emit(newList)
                _afterDeleteCurrentReadBook.emit(Unit)
            }
        }
    }

    fun deleteBookFromNextInLine(bookId: Long) {
        val nextInLineBooks = nextInLineBooks.value
        val findItem = nextInLineBooks?.find { it.id == bookId }
        if (findItem != null) {
            val newList = nextInLineBooks.toMutableList().apply {
                remove(findItem)
            }
            viewModelScope.launch {
                delay(100)
                _nextInLineBooks.emit(newList)
                _afterDeleteCurrentReadBook.emit(Unit)
            }
        }
    }
}