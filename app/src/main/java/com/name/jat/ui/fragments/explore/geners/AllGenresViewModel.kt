package com.name.jat.ui.fragments.explore.geners

import androidx.lifecycle.viewModelScope
import com.name.domain.model.GenreDataModel
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllGenresViewModel : BaseViewModel() {

    private val _genreDataList: MutableStateFlow<List<GenreDataModel>?> by lazy {
        MutableStateFlow(
            null
        )
    }
    val genreDataList = _genreDataList.asStateFlow()

    fun getGenreDataList() {
        val genreList = listOf(
            GenreDataModel(14, "New Adults"),
            GenreDataModel(15, "Fantasy"),
            GenreDataModel(16, "Romance"),
            GenreDataModel(17, "New Adults"),
            GenreDataModel(18, "Fantasy"),
            GenreDataModel(19, "Romance"),
            GenreDataModel(14, "New Adults"),
            GenreDataModel(15, "Fantasy"),
            GenreDataModel(16, "Romance"),
            GenreDataModel(17, "New Adults"),
            GenreDataModel(18, "Fantasy"),
            GenreDataModel(19, "Romance")
        )
        viewModelScope.launch {
            _genreDataList.emit(genreList)
        }
    }
}