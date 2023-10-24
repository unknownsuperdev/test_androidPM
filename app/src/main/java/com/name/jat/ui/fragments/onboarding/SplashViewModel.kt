package com.name.jat.ui.fragments.onboarding

import androidx.lifecycle.viewModelScope
import com.name.domain.interactors.GetIsOpenedWelcomeScreenUseCase
import com.name.jat.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class SplashViewModel(
    private val getIsOpenedWelcomeScreenUseCase: GetIsOpenedWelcomeScreenUseCase
) : BaseViewModel() {
    private val _durationEnded = MutableSharedFlow<Unit?>()
    val durationEnded = _durationEnded.asSharedFlow()

  private val _isOpenedWelcomeScreens = MutableSharedFlow<Boolean?>()
    val isOpenedWelcomeScreens = _isOpenedWelcomeScreens.asSharedFlow()

    fun startTimer() {
        viewModelScope.launch {
            delay(3000)
            _durationEnded.emit(Unit)
        }
    }

    fun getIsOpenedWelcomeScreens(){
        viewModelScope.launch {
           getIsOpenedWelcomeScreenUseCase().collect{
               _isOpenedWelcomeScreens.emit(it)
            }
        }
    }
}