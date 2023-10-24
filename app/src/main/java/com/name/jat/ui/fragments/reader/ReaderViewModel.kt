package com.name.jat.ui.fragments.reader

import androidx.lifecycle.viewModelScope
import com.name.domain.interactors.BookSettingsGetUseCase
import com.name.domain.interactors.BookSettingsInsertUseCase
import com.name.domain.model.BookSettingsData
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReaderViewModel(
    private val useCaseSettingsGet: BookSettingsGetUseCase,
    private val useCaseSettingsInsert: BookSettingsInsertUseCase
) : BaseViewModel() {

    private val _timer = MutableSharedFlow<Int>()
    val timer = _timer.asSharedFlow()
    private var currentPagePosition = 0

    private val _bookSettings = MutableStateFlow<BookSettingsData?>(null)
    val bookSettings = _bookSettings.asStateFlow()

    var ticker: Int = 0
        set(value) {
            field = value
            viewModelScope.launch {
                while (field > 0) {
                    delay(1000)
                    field--
                }
                _timer.emit(0)
            }
        }

    fun getBookSettings() {
        viewModelScope.launch {
            val result = useCaseSettingsGet()
            _bookSettings.emit(result)
        }
    }

    fun changeBookSettings(bookSettingsData: BookSettingsData?) {
        viewModelScope.launch {
            bookSettingsData?.let {
                useCaseSettingsInsert(it)
            }
        }
        _bookSettings.value = bookSettingsData
    }

    fun countPercentageOfReadPages(countOfReadPage: Int, bookPageCount: Int) =
        "${countOfReadPage * 100 / bookPageCount}%"

    fun setCurrentPagePosition(position: Int) {
        currentPagePosition = position
    }

    fun getCurrentPagePosition() = currentPagePosition
}