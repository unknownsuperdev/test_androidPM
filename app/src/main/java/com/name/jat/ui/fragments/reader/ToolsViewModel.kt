package com.name.jat.ui.fragments.reader

import androidx.lifecycle.viewModelScope
import com.name.domain.interactors.BookSettingsGetUseCase
import com.name.domain.interactors.TextFontsUseCase
import com.name.domain.model.BookSettingsData
import com.name.domain.model.TextFontTypesModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToolsViewModel(
    private val useCaseSettingsGet: BookSettingsGetUseCase,
    private val textFontsUseCase: TextFontsUseCase
) : BaseViewModel() {
    private val maxSize = 24
    private val minSize = 15

    private val _fontsTypes = MutableStateFlow<List<TextFontTypesModel>?>(null)
    val fontsTypes = _fontsTypes.asStateFlow()

    private val _bookSettings = MutableStateFlow<BookSettingsData?>(null)
    val bookSettings = _bookSettings.asStateFlow()

    fun getFontTypes() {
        viewModelScope.launch {
            val fontsList = bookSettings.value?.let { textFontsUseCase(it.textType) }
            _fontsTypes.emit(fontsList)
        }
    }

    fun getBookSettings() {
        viewModelScope.launch {
            val result = useCaseSettingsGet()
            _bookSettings.emit(result)
        }
    }

    fun getSettingsData() = bookSettings.value

    fun changeBookSettings(bookSettingsData: BookSettingsData?) {
        _bookSettings.value = bookSettingsData
    }

    fun changeTextSize(textSize: Int, isAdd: Boolean): Int {
        var sizeOfText = textSize
        return when {
            sizeOfText < maxSize && isAdd -> {
                ++sizeOfText
            }
            sizeOfText > minSize && !isAdd -> {
                --sizeOfText
            }
            else -> -1
        }
    }

    fun changeFontType(itemId: Long) {
        val fontType = fontsTypes.value
        var newList = fontType?.toMutableList() ?: mutableListOf()
        newList = newList.toMutableList().apply {
            this.forEachIndexed { index, item ->
                val itemCopy = item.copy(isSelected = item.id == itemId)
                this[index] = itemCopy
            }
        }

        viewModelScope.launch {
            _fontsTypes.emit(newList)
        }
    }
}