package com.fiction.me.ui.fragments.explore.geners

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetAllGenresUseCase
import com.fiction.domain.model.GenreDataModel
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AllGenresViewModel(
    private val getAllGenresUseCase: GetAllGenresUseCase
) : BaseViewModel() {

    private val _genreDataList = MutableStateFlow<List<GenreDataModel>?>(null)
    val genreDataList = _genreDataList.asStateFlow()

    fun getGenreDataList() {

        viewModelScope.launch {
            when(val result = getAllGenresUseCase()){
               is ActionResult.Success -> {
                   _genreDataList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }
}