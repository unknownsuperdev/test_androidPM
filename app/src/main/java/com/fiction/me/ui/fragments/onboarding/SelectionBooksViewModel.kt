package com.fiction.me.ui.fragments.onboarding

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetBooksForYouUseCase
import com.fiction.domain.model.onboarding.BooksForYou
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SelectionBooksViewModel(
    private val getBooksForYouUseCase: GetBooksForYouUseCase
) : BaseViewModel() {
    private val _images = MutableStateFlow<List<String>?>(null)
    val images = _images.asStateFlow()

    private val _bookForYou = MutableStateFlow<BooksForYou?>(null)
    val bookForYou = _bookForYou.asStateFlow()

    private val books: MutableList<BooksForYou> = mutableListOf()
    var isFirstLaunch = true

    fun getBooksForReading(position: Int): Pair<Long, Long>? {
        return if (books.size >= position) {
            val bookId = books[position].id
            val chapterId = books[position].firstChapter.id
            Pair(bookId, chapterId)
        } else null
    }

    fun getBooksForYou(position: Int) {
        viewModelScope.launch {
            when (val result = getBooksForYouUseCase()) {
                is ActionResult.Success -> {
                    books.addAll(result.result)
                    val sliderImages = mutableListOf<String>()
                    result.result.forEach {
                        sliderImages.add(it.cover)
                    }
                    _images.emit(sliderImages)
                    _bookForYou.emit(result.result[position])
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun slideBooks(position: Int) {
        viewModelScope.launch {
            if (books.size >= position)
                _bookForYou.emit(books[position])
        }
    }
}