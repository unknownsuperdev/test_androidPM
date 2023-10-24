package com.fiction.me.ui.fragments.purchase

import androidx.lifecycle.viewModelScope
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class GetCoinForNotPayerUserViewModel : BaseViewModel() {

    private val _getCoinProgress = MutableSharedFlow<Unit>()
    val getCoinProgress = _getCoinProgress.asSharedFlow()

    var isStartProgress: Boolean = false
        set(value) {
            field = value
            viewModelScope.launch {
                delay(1000)
                _getCoinProgress.emit(Unit)
            }
        }
}