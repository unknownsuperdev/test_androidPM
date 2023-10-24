package com.name.jat.ui.fragments.profile.settings

import androidx.lifecycle.viewModelScope
import com.name.core.ActionResult
import com.name.domain.interactors.GetProfileInfoUseCase
import com.name.domain.interactors.UpdateProfileUseCase
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AutoUnlockViewModel(
    private val getProfileInfoUseCase: GetProfileInfoUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    private val _isTurnOnAutoUnlock = MutableStateFlow<Boolean?>(null)
    val isTurnOnAutoUnlock = _isTurnOnAutoUnlock.asStateFlow()

    fun getLockState() {
        viewModelScope.launch {
            when (val result = getProfileInfoUseCase()) {
                is ActionResult.Success -> {
                    _isTurnOnAutoUnlock.emit(result.result.setting.autoUnlockPaid)
                }
                is ActionResult.Error -> {
                    // Error handling
                }
            }
        }
    }

    fun changeAutoUnlockMode(isTurnOnAutoUnlock: Boolean) {
        viewModelScope.launch {
            when (updateProfileUseCase(readingMode = isTurnOnAutoUnlock)) {
                is ActionResult.Success -> {
                    _isTurnOnAutoUnlock.emit(isTurnOnAutoUnlock)
                }
                is ActionResult.Error -> {
                    // Error handling
                }
            }
        }
    }
}