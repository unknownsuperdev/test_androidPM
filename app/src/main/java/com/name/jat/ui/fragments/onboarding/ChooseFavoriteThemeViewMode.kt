package com.name.jat.ui.fragments.onboarding

import androidx.lifecycle.viewModelScope
import com.name.domain.model.FavoriteThemeData
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChooseFavoriteThemeViewMode : BaseViewModel() {
    private val _themeList = MutableStateFlow<List<FavoriteThemeData>?>(null)
    val themeList = _themeList.asStateFlow()

    fun getTheme() {
        val themes = listOf(
            FavoriteThemeData(1, "Billionaire"),
            FavoriteThemeData(2, "Alpha"),
            FavoriteThemeData(3, "Vampire"),
            FavoriteThemeData(4, "Forbidden Love"),
            FavoriteThemeData(5, "Secret Affair"),
            FavoriteThemeData(6, "My Ex"),
            FavoriteThemeData(7, "Mr. SEO"),
            FavoriteThemeData(8, "Best Friends"),
            FavoriteThemeData(9, "Playboy"),
            FavoriteThemeData(10, "Arranged Marriage"),
        )

        viewModelScope.launch {
            _themeList.emit(themes)
        }
    }

    fun updateThemeSelectionState(id: Long, isSelected: Boolean) {
        val findItem = themeList.value?.find { it.id == id }
        val index = if (findItem != null) themeList.value?.indexOf(findItem) else -1

        if (index != -1 && index != null) {
            val updatedList = themeList.value?.toMutableList().apply {
                this?.set(index, this[index].copy(isSelected = isSelected))
            }
            viewModelScope.launch {
                _themeList.emit(updatedList)
            }
        }
    }

    fun getFilteredListById() = themeList.value?.filter {
        it.isSelected
    }

}