package com.name.jat.ui.fragments.profile.settings

import androidx.lifecycle.viewModelScope
import com.name.core.ActionResult
import com.name.domain.interactors.GetProfileInfoUseCase
import com.name.domain.interactors.UpdateProfileUseCase
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ReadingModeViewModel(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    private val _isTeenager = MutableStateFlow<Boolean?>(null)
    val isTeenager = _isTeenager.asStateFlow()

    fun getReadingMode() {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    _isTeenager.emit(result.result.setting.readingMode)
                }
                is ActionResult.Error -> {
                    // Error handling
                }
            }
        }
    }

    fun changeReadingMode(isTeenager: Boolean){
        viewModelScope.launch {
            when(updateProfileUseCase(readingMode = isTeenager)){
                is ActionResult.Success -> {
                    _isTeenager.emit(isTeenager)
                }
                is ActionResult.Error -> {
                    // Error handling
                }
            }
        }
    }
}