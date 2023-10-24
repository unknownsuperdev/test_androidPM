package com.fiction.me.ui.fragments.onboarding

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.analytics.api.Events.USER_PROPERTY__FAVORITE_THEMES
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.*
import com.fiction.domain.model.onboarding.FavoriteThemeTags
import com.fiction.domain.model.onboarding.OnBoardingSettingData
import com.fiction.me.appbase.viewmodel.BaseViewModel
import com.fiction.me.utils.Events
import com.fiction.me.utils.Events.Companion.TAG_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChooseFavoriteThemeViewModel(
    private val getFavoriteTagsUseCase: GetFavoriteTagsUseCase,
    private val updateOnBoardingSettingUseCase: UpdateOnBoardingSettingUseCase,
    private val insertOnBoardingSettingUseCase: InsertOnBoardingSettingUseCase,
    private val getOnBoardingSettingUseCase: GetOnBoardingSettingUseCase,
    private val deleteOnBoardingSettingUseCase: DeleteOnBoardingSettingUseCase,
) : BaseViewModel() {
    private val _themeList = MutableStateFlow<List<FavoriteThemeTags>?>(null)
    val themeList = _themeList.asStateFlow()
    private var onBoardingSettingId = ""
    init {
        getOnBoardingSetting()
    }
    fun getThemes() {
        viewModelScope.launch {
            when (val result = getFavoriteTagsUseCase()) {
                is ActionResult.Success -> {
                    _themeList.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
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
        insertSelectedThemes()
    }

    fun getFilteredListById() = themeList.value?.filter {
        it.isSelected
    }

    fun updateFavoriteTagsSelection() {
        val selectedTags = mutableListOf<FavoriteThemeTags>()
        getFilteredListById()?.forEach {
            selectedTags.add(it)
        }
        if (selectedTags.isEmpty()) return
        setUserPropertyEvent(mapOf(USER_PROPERTY__FAVORITE_THEMES to selectedTags.joinToString { it.title }))
        trackEvents(Events.FAVOURITE_THEME_SCREEN_CONTINUE_BUTTON_CLICKED, hashMapOf(TAG_NAME to selectedTags.joinToString { it.title }))
        viewModelScope.launch {
            when (val result = updateOnBoardingSettingUseCase(favoriteTagIds = selectedTags.map { it.id })) {
                is ActionResult.Success -> {
                    Log.i("UpdateSettings", "updateFavoriteTagsSelection: ${result.result}")
                }
                is ActionResult.Error -> {
                    Log.i("UpdateSettings", "updateFavoriteTagsSelection: ${result.errors}")
                    // TODO() //Error Handling
                }
            }
        }
    }
    private fun insertSelectedThemes() {
        viewModelScope.launch {
            val selectedTags = mutableListOf<Long>()
            getFilteredListById()?.forEach {
                selectedTags.add(it.id)
            }
            insertOnBoardingSettingUseCase(OnBoardingSettingData(id = onBoardingSettingId, favoriteThemeTags = selectedTags))
        }
    }

    private fun getOnBoardingSetting() {
        viewModelScope.launch {
            val onBoardingSetting = getOnBoardingSettingUseCase()
            onBoardingSettingId = onBoardingSetting.id
        }
    }

    fun deleteOnBoardingSetting(){
        viewModelScope.launch {
            deleteOnBoardingSettingUseCase()
        }
    }

}
