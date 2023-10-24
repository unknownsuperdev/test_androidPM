package com.fiction.me.ui.fragments.profile.settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.GetProfileInfoUseCase
import com.fiction.domain.interactors.UpdateProfileUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
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
                    Log.i("TurnOnAutoUnlock", "getLockState: ${result.result.setting.autoUnlockPaid}")
                    _isTurnOnAutoUnlock.emit(result.result.setting.autoUnlockPaid)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

    fun changeAutoUnlockMode(isTurnOnAutoUnlock: Boolean) {
        viewModelScope.launch {
            when (updateProfileUseCase(autoUnlockPaid = isTurnOnAutoUnlock)) {
                is ActionResult.Success -> {
                    _isTurnOnAutoUnlock.emit(isTurnOnAutoUnlock)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }
}