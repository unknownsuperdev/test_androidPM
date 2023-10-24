package com.fiction.me.ui.fragments.mainlibrary

import androidx.lifecycle.viewModelScope
import com.amplitude.api.Constants.LIBRARY
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetCurrentReadingBooksUseCase
import com.fiction.domain.interactors.GetFinishedReadBooksUseCase
import com.fiction.domain.interactors.GetLibAlsoLikeBooksUseCase
import com.fiction.domain.interactors.GetLibraryBooksUseCase
import com.fiction.domain.model.BooksWithReadProgressBookData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events.Companion.CURRENT_READS
import com.fiction.me.utils.Events.Companion.PLACEMENT
import com.fiction.me.utils.Events.Companion.SECTION
import com.fiction.me.utils.Events.Companion.SEE_ALL_CLICKED
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainLibraryViewModel(
    private val getCurrentReadingBooksUseCase: GetCurrentReadingBooksUseCase,
    private val getFinishedReadBooksUseCase: GetFinishedReadBooksUseCase,
    private val getBooksLibraryUseCase: GetLibraryBooksUseCase,
    private val getLibAlsoLikeBooksUseCase: GetLibAlsoLikeBooksUseCase,
) : BaseViewModel() {

    init {
        cachingData()
    }

    private val _currentReadBooks = MutableStateFlow<List<BooksWithReadProgressBookData>?>(null)
    val currentReadBooks = _currentReadBooks.asStateFlow()

    private val _afterDeleteCurrentReadBook = MutableSharedFlow<Unit?>()
    val afterDeleteCurrentReadBook = _afterDeleteCurrentReadBook.asSharedFlow()
    var message = ""

    fun getCurrentReadBooks() {
        viewModelScope.launch {
            val cacheData = getCurrentReadingBooksUseCase.getCachedData()
            _currentReadBooks.emit(cacheData)
            when (val result = getCurrentReadingBooksUseCase(true)) {
                is ActionResult.Success -> {
                    _currentReadBooks.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    private fun cachingData() {
        viewModelScope.launch {
            getFinishedReadBooksUseCase()
            getBooksLibraryUseCase()
            getLibAlsoLikeBooksUseCase()
        }
    }

    fun deleteBookFromLib(bookId: Long) {
        val currentReadBooks = currentReadBooks.value
        val findItem = currentReadBooks?.find { it.id == bookId }
        if (findItem != null) {
            val newList = currentReadBooks.toMutableList().apply {
                remove(findItem)
            }
            viewModelScope.launch {
                delay(100)
                _afterDeleteCurrentReadBook.emit(Unit)
                _currentReadBooks.emit(newList)
            }
        }
    }

    fun sendEventSeeAllClick() {
        trackEvents(
            SEE_ALL_CLICKED, hashMapOf(
                PLACEMENT to LIBRARY,
                SECTION to CURRENT_READS
            )
        )
    }
}