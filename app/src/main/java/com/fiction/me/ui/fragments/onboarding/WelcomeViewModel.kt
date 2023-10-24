package com.fiction.me.ui.fragments.onboarding

import androidx.lifecycle.viewModelScope
import com.fiction.domain.interactors.SetIsDataRestoredUseCase
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

class WelcomeViewModel(
    private val setIsDataRestoredUseCase: SetIsDataRestoredUseCase,
) : BaseViewModel() {

    init {
        setDataRestored()
    }

    private fun setDataRestored() {
        viewModelScope.launch {
            setIsDataRestoredUseCase(false)
        }
    }
}