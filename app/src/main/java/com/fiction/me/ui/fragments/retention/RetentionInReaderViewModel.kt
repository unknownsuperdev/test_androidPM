package com.fiction.me.ui.fragments.retention

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetSuggestionBooksUseCase
import com.fiction.domain.model.RetentionScreenBookItem
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RetentionInReaderViewModel(
    private val getSuggestionBooksUseCase: GetSuggestionBooksUseCase
) : BaseViewModel() {
    private val _retentionScreenBooks = MutableStateFlow<List<RetentionScreenBookItem>?>(null)
    val retentionScreenBooks = _retentionScreenBooks.asStateFlow()
    var isVisibleReadButton = false
    var readingBookId: Long = -1L
    var selectedBookTitle = ""

    fun getRetentionScreenBooks(bookId: Long) {
        viewModelScope.launch {
            readingBookId = bookId
            when (val result = getSuggestionBooksUseCase(readingBookId)) {
                is ActionResult.Success -> {
                    _retentionScreenBooks.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateBookSelectionState(bookId: Long, title: String) {
        val books = _retentionScreenBooks.value
        readingBookId = bookId
        selectedBookTitle = title
        val newList = books?.map { it.copy(isSelected = it.id == bookId) }
        isVisibleReadButton = true
        viewModelScope.launch {
            _retentionScreenBooks.emit(newList)
        }
    }
}