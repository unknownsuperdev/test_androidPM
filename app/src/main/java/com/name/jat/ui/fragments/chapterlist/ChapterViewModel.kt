package com.name.jat.ui.fragments.chapterlist

import androidx.lifecycle.viewModelScope
import com.name.domain.model.ChaptersDataModel
import com.name.domain.model.ChaptersModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChapterViewModel : BaseViewModel() {
    private val _chaptersList = MutableStateFlow<ChaptersModel?>(null)
    val chaptersList = _chaptersList.asStateFlow()

    fun getChapterListData() {
        val chapterList = ChaptersModel(
            123456, -1, listOf(
                ChaptersDataModel(1, "My life", false, false),
                ChaptersDataModel(2, "Friend", false, true),
                ChaptersDataModel(3, "Before you come", true, false),
                ChaptersDataModel(4, "My secrets", true, false),
                ChaptersDataModel(5, "Moonlight", true, false),
                ChaptersDataModel(6, "The best day of my life", true, false),
                ChaptersDataModel(7, "Need to try", true, false),
                ChaptersDataModel(8, "Be better", true, false),
                ChaptersDataModel(9, "Summer vacation", true, false),
                ChaptersDataModel(10, "My God", true, false),
                ChaptersDataModel(11, "My life", true, false),
                ChaptersDataModel(12, "Friend", true, false),
                ChaptersDataModel(13, "Before you come", true, false),
                ChaptersDataModel(14, "My secrets", true, false),
                ChaptersDataModel(15, "Moonlight", true, false),
                ChaptersDataModel(16, "The best day of my life", true, false),
                ChaptersDataModel(17, "Need to try", true, false),
                ChaptersDataModel(18, "Be better", true, false),
                ChaptersDataModel(19, "Summer vacation", true, false),
                ChaptersDataModel(20, "My God", true, false)
            )
        )

        viewModelScope.launch {
            _chaptersList.emit(
                chapterList
            )
        }
    }
}

