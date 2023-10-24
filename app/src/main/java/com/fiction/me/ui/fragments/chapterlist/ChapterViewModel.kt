package com.fiction.me.ui.fragments.chapterlist

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetBookChaptersUseCase
import com.fiction.domain.model.ChaptersDataModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChapterViewModel(
    private val getBookChaptersUseCase: GetBookChaptersUseCase
) : BaseViewModel() {

    private val _chaptersList = MutableStateFlow<List<ChaptersDataModel>?>(null)
    val chaptersList = _chaptersList.asStateFlow()

    private val _updatedList = MutableSharedFlow<Unit>()
    val updatedList = _updatedList.asSharedFlow()
    var bookId: Long = -1L

    fun getChapters(bookId: Long) {
        if (bookId == -1L) return
        this.bookId = bookId
        viewModelScope.launch {
            when (val result = getBookChaptersUseCase(bookId)) {
                is ActionResult.Success -> {
                    _chaptersList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateChapter(lastReadChapter: Long) {
        viewModelScope.launch {
            when (getBookChaptersUseCase(bookId, lastReadChapterId = lastReadChapter)) {
                is ActionResult.Success -> {
                    _updatedList.emit(Unit)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }
}

