package com.fiction.me.ui.fragments.purchase

import androidx.lifecycle.viewModelScope
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class SpecialOfferGetCoinViewModel : BaseViewModel() {
    private val _timer = MutableSharedFlow<Int>()
    val timer = _timer.asSharedFlow()

    private val _getCoinProgress = MutableSharedFlow<Unit>()
    val getCoinProgress = _getCoinProgress.asSharedFlow()

    var ticker: Int = 0
        set(value) {
            field = value
            viewModelScope.launch {
                while (field > 0) {
                    delay(1000)
                    field--
                    _timer.emit(field)
                }
            }
        }

    var isStartProgress: Boolean = false
        set(value) {
            field = value
            viewModelScope.launch {
                delay(1000)
                _getCoinProgress.emit(Unit)
            }
        }
}
