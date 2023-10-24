package com.fiction.me.ui.fragments.purchase

import androidx.lifecycle.viewModelScope
import com.fiction.core.ActionResult
import com.fiction.domain.interactors.UpdateProfileUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AutoUnlockStateViewModel(
    private val updateProfileUseCase: UpdateProfileUseCase
) : BaseViewModel() {

    private val _isTurnOnAutoUnlock = MutableSharedFlow<Unit>()
    val isTurnOnAutoUnlock = _isTurnOnAutoUnlock.asSharedFlow()

    fun changeAutoUnlockMode(isTurnOnAutoUnlock: Boolean) {
        viewModelScope.launch {
            when (updateProfileUseCase(autoUnlockPaid = isTurnOnAutoUnlock)) {
                is ActionResult.Success -> {
                    _isTurnOnAutoUnlock.emit(Unit)
                }
                is ActionResult.Error -> {
                    // TODO() //Error Handling
                }
            }
        }
    }

}