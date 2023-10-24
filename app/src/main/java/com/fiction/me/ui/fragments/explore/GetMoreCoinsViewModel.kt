package com.fiction.me.ui.fragments.explore

import androidx.lifecycle.viewModelScope
import com.fiction.me.appbase.viewmodel.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class GetMoreCoinsViewModel : BaseViewModel() {

    private val _timer = MutableSharedFlow<Int>()
    val timer = _timer.asSharedFlow()

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
}