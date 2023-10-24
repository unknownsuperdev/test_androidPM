package com.fiction.me.ui.fragments.reader

import androidx.lifecycle.viewModelScope
import com.fiction.domain.interactors.BookSettingsGetUseCase
import com.fiction.domain.interactors.TextFontsUseCase
import com.fiction.domain.model.BookSettingsData
import com.fiction.domain.model.TextFontTypesModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events.Companion.BRIGHTNESS
import com.fiction.me.utils.Events.Companion.COLOR_THEME
import com.fiction.me.utils.Events.Companion.LINE_HEIGHT
import com.fiction.me.utils.Events.Companion.READING_TOOLS_SCREEN_SHOWN
import com.fiction.me.utils.Events.Companion.TEXT_SIZE
import com.fiction.me.utils.Events.Companion.TEXT_TYPE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ToolsViewModel(
    private val useCaseSettingsGet: BookSettingsGetUseCase,
    private val textFontsUseCase: TextFontsUseCase
) : BaseViewModel() {
    private val maxSize = 26
    private val minSize = 14

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
            sendToolsEvent(result)
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

    private fun sendToolsEvent(bookSettingsData: BookSettingsData){

        trackEvents(
            READING_TOOLS_SCREEN_SHOWN,
            hashMapOf(
                BRIGHTNESS to bookSettingsData.brightness,
                COLOR_THEME to bookSettingsData.colorTheme.toString().lowercase(),
                TEXT_TYPE to bookSettingsData.textType.toString().lowercase(),
                TEXT_SIZE to bookSettingsData.textSize,
                LINE_HEIGHT to bookSettingsData.lineHeight.toString().lowercase()
            )
        )
    }
}