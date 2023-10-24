package com.fiction.me.ui.fragments.onboarding

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetFavoriteReadingTimeUseCase
import com.fiction.domain.interactors.GetOnBoardingSettingUseCase
import com.fiction.domain.interactors.InsertOnBoardingSettingUseCase
import com.fiction.domain.interactors.UpdateOnBoardingSettingUseCase
import com.fiction.domain.model.onboarding.FavoriteReadingTime
import com.fiction.domain.model.onboarding.OnBoardingSettingData
import com.fiction.me.appbase.viewmodel.BaseViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PickReadingTimeViewModel(
    private val getFavoriteReadingTimeUseCase: GetFavoriteReadingTimeUseCase,
    private val updateOnBoardingSettingUseCase: UpdateOnBoardingSettingUseCase,
    private val insertOnBoardingSettingUseCase: InsertOnBoardingSettingUseCase,
    private val getOnBoardingSettingUseCase: GetOnBoardingSettingUseCase,
) : BaseViewModel() {

    private val _readingTime = MutableStateFlow<List<FavoriteReadingTime>?>(null)
    val readingTime = _readingTime.asStateFlow()

    private val _selectedReadingTimeId = MutableStateFlow<Int?>(null)
    val selectedReadingTimeId = _selectedReadingTimeId.asStateFlow()

    private var onBoardingSettingsId = ""

    fun getFavoriteReadingTime() {
        viewModelScope.launch {
            when (val result = getFavoriteReadingTimeUseCase()) {
                is ActionResult.Success -> {
                    _readingTime.emit(result.result)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun updateReadingTimeSelection(selectedId: Int?) {
        if (selectedId == null) return
        viewModelScope.launch {
            when (val result = updateOnBoardingSettingUseCase(readingTimeId = selectedId)) {
                is ActionResult.Success -> {
                    Log.i("UpdateSettings", "updateReadingTimeSelection: ${result.result}")
                }
                is ActionResult.Error -> {
                    Log.i("UpdateSettings", "updateReadingTimeSelection: ${result.errors}")
                    // TODO() //Error Handling
                }
            }
        }
    }
    fun insertOnBoardingSetting(selectedReadingTimeId: Int) {
        viewModelScope.launch {
            insertOnBoardingSettingUseCase(OnBoardingSettingData(id = onBoardingSettingsId,readingTimeId = selectedReadingTimeId))
        }
    }

    fun getOnBoardingSetting() {
        viewModelScope.launch {
            val onBoardingSetting = getOnBoardingSettingUseCase()
            onBoardingSettingsId = onBoardingSetting.id
            onBoardingSetting.readingTimeId?.let {
                _selectedReadingTimeId.emit(it)
            }
        }
    }
}